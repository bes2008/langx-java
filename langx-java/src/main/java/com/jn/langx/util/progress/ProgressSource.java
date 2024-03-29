package com.jn.langx.util.progress;

import com.jn.langx.AbstractNameable;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.NotEmpty;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Maths;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.id.UuidGenerator;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

/**
 * 这个类可以单独使用，也可以与 ProgressTracer, ProgressListener, ProgressEvent 一起配合使用
 *
 * @since 4.4.2
 */
public class ProgressSource extends AbstractNameable {
    private static final Logger logger = Loggers.getLogger(ProgressSource.class);
    private static final String SEP = "__PROGRESS__";
    @NotEmpty
    private String eventDomain;
    @NotEmpty
    private String id;
    /**
     * current progress
     */
    private long progress;

    /**
     * 正常情况下，该值应该为正值；若为 负值，则代表该值还没初始化
     */
    private long expected;

    @NonNull
    private State state;

    @Nullable
    private ProgressTracer tracer;

    @Nullable
    private Object ref;

    public ProgressSource(long expected) {
        this(UuidGenerator.INSTANCE.get(), expected);
    }

    public ProgressSource(String id, long expected) {
        this("common", id, expected);
    }

    public ProgressSource(String eventDomain, String id) {
        this(eventDomain, id, -1L);
    }

    public ProgressSource(String eventDomain, String id, long expected) {
        this(eventDomain, id, expected, null, null);
    }

    public ProgressSource(String eventDomain, String id, long expected, Object ref, ProgressTracer tracer) {
        Preconditions.checkNotNullArgument(eventDomain, "eventDomain");
        this.eventDomain = eventDomain;
        Preconditions.checkNotNullArgument(id, "id");
        this.id = id;
        this.setName(this.eventDomain + SEP + this.id);
        this.expected = expected;
        this.progress = 0L;
        this.state = State.INITIALED;

        this.ref = ref;
        this.tracer = tracer;
    }

    public void start() {
        if(state == State.INITIALED) {
            if (this.tracer != null) {
                this.tracer.begin(this);
            }
            this.state = State.UPDATING;
            if (logger.isDebugEnabled()) {
                logger.debug(this.toString());
            }
        }
    }

    public void forward(long increment) {
        update(getProgress() + increment);
    }

    public void update(long progress) {
        this.update(progress, -1);
    }

    /**
     * @param progress 设置当前进度； 若值小于 0，则不设置
     * @param expected 设置期望值，若值 小于0，则不设置
     */
    public void update(long progress, long expected) {
        Preconditions.checkState(state == State.UPDATING, "not in updating: " + getName());

        if (progress > 0L) {
            this.progress = progress;
        }
        if (expected > 0L) {
            Preconditions.checkArgument(expected >= this.progress);
            this.expected = expected;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(this.toString());
        }
        if (this.tracer != null) {
            this.tracer.updateProcess(this);
        }
    }

    public void finish() {
        if (this.state != State.FINISHED) {
            this.state = State.FINISHED;
            if (logger.isDebugEnabled()) {
                logger.debug(this.toString());
            }
            if (this.tracer != null) {
                this.tracer.finish(this);
            }
        }
    }

    public boolean isSuccess(){
        return finished() && (getExpected() < 0 || getProgress() == getExpected());
    }

    public State getState() {
        return this.state;
    }

    public boolean started() {
        return this.state != State.INITIALED;
    }

    public Object getRef() {
        return ref;
    }

    public boolean finished() {
        return this.state == State.FINISHED;
    }

    public long getProgress() {
        return this.progress;
    }

    public long getExpected() {
        return this.expected;
    }

    public String getEventDomain() {
        return this.eventDomain;
    }

    public double percent() {
        return percent(0);
    }

    public String percentAsString(){
        return percentAsString(0);
    }

    public double percent(int precision) {
        double p = (1d * this.progress) / this.expected * 100d;
        p = Maths.formatPrecision(p, precision);
        return p;
    }

    public String percentAsString(int precision){
        double p = (1d * this.progress) / this.expected * 100d;
        String string = Maths.formatPrecisionAsString(p, precision);
        return string + "%";
    }

    @Override
    public String toString() {
        return StringTemplates.formatWithPlaceholder("progress trace: {}, current: {}, expected: {}, percent: {}, state: {}", getName(), getProgress(), getExpected(), percentAsString(), getState());
    }

    private enum State {
        INITIALED, // 初始状态，还未开始
        UPDATING, // 更新中
        FINISHED // 已完成
    }
}
