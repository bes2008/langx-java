package com.jn.langx.util.concurrent.completion;


import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Function2;

import java.util.concurrent.Executor;

/**
 * A stage of a possibly asynchronous computation, that performs an
 * action or computes a value when another CompletionStage completes.
 * A stage completes upon termination of its computation, but this may
 * in turn trigger other dependent stages.  The functionality defined
 * in this interface takes only a few basic forms, which expand out to
 * a larger set of methods to capture a range of usage styles:
 * <p>
 * <ul>
 * <p>
 * <li>The computation performed by a stage may be expressed as a
 * Function, Consumer, or Runnable (using methods with names including
 * <em>apply</em>, <em>accept</em>, or <em>run</em>, respectively)
 * depending on whether it requires arguments and/or produces results.
 * For example:
 * <pre> {@code
 * stage.thenApply(x -> square(x))
 *      .thenAccept(x -> System.out.print(x))
 *      .thenRun(() -> System.out.println());}</pre>
 * <p>
 * An additional form (<em>compose</em>) allows the construction of
 * computation pipelines from functions returning completion stages.
 * <p>
 * <p>Any argument to a stage's computation is the outcome of a
 * triggering stage's computation.
 * <p>
 * <li>One stage's execution may be triggered by completion of a
 * single stage, or both of two stages, or either of two stages.
 * Dependencies on a single stage are arranged using methods with
 * prefix <em>then</em>. Those triggered by completion of
 * <em>both</em> of two stages may <em>combine</em> their results or
 * effects, using correspondingly named methods. Those triggered by
 * <em>either</em> of two stages make no guarantees about which of the
 * results or effects are used for the dependent stage's computation.
 * <p>
 * <li>Dependencies among stages control the triggering of
 * computations, but do not otherwise guarantee any particular
 * ordering. Additionally, execution of a new stage's computations may
 * be arranged in any of three ways: default execution, default
 * asynchronous execution (using methods with suffix <em>async</em>
 * that employ the stage's default asynchronous execution facility),
 * or custom (via a supplied { Executor}).  The execution
 * properties of default and async modes are specified by
 * CompletionStage implementations, not this interface. Methods with
 * explicit Executor arguments may have arbitrary execution
 * properties, and might not even support concurrent execution, but
 * are arranged for processing in a way that accommodates asynchrony.
 * <p>
 * <li>Two method forms ({ #handle handle} and {
 * #whenComplete whenComplete}) support unconditional computation
 * whether the triggering stage completed normally or exceptionally.
 * Method { #exceptionally exceptionally} supports computation
 * only when the triggering stage completes exceptionally, computing a
 * replacement result, similarly to the java {@code catch} keyword.
 * In all other cases, if a stage's computation terminates abruptly
 * with an (unchecked) exception or error, then all dependent stages
 * requiring its completion complete exceptionally as well, with a
 * { CompletionException} holding the exception as its cause.  If
 * a stage is dependent on <em>both</em> of two stages, and both
 * complete exceptionally, then the CompletionException may correspond
 * to either one of these exceptions.  If a stage is dependent on
 * <em>either</em> of two others, and only one of them completes
 * exceptionally, no guarantees are made about whether the dependent
 * stage completes normally or exceptionally. In the case of method
 * {@code whenComplete}, when the supplied action itself encounters an
 * exception, then the stage completes exceptionally with this
 * exception unless the source stage also completed exceptionally, in
 * which case the exceptional completion from the source stage is
 * given preference and propagated to the dependent stage.
 * <p>
 * </ul>
 * <p>
 * <p>All methods adhere to the above triggering, execution, and
 * exceptional completion specifications (which are not repeated in
 * individual method specifications). Additionally, while arguments
 * used to pass a completion result (that is, for parameters of type
 * {@code T}) for methods accepting them may be null, passing a null
 * value for any other parameter will result in a {
 * NullPointerException} being thrown.
 * <p>
 * <p>Method form { #handle handle} is the most general way of
 * creating a continuation stage, unconditionally performing a
 * computation that is given both the result and exception (if any) of
 * the triggering CompletionStage, and computing an arbitrary result.
 * Method { #whenComplete whenComplete} is similar, but preserves
 * the result of the triggering stage instead of computing a new one.
 * Because a stage's normal result may be {@code null}, both methods
 * should have a computation structured thus:
 * <p>
 * <pre>{@code (result, exception) -> {
 *   if (exception == null) {
 *     // triggering stage completed normally
 *   } else {
 *     // triggering stage completed exceptionally
 *   }
 * }}</pre>
 * <p>
 * <p>This interface does not define methods for initially creating,
 * forcibly completing normally or exceptionally, probing completion
 * status or results, or awaiting completion of a stage.
 * Implementations of CompletionStage may provide means of achieving
 * such effects, as appropriate.  Method { #toCompletableFuture}
 * enables interoperability among different implementations of this
 * interface by providing a common conversion type.
 *
 * @author Doug Lea
 */
public interface CompletionStep<T> {

    /**
     * Returns a new CompletionStage that, when this stage completes
     * normally, is executed with this stage's result as the argument
     * to the supplied function.
     * <p>
     * <p>This method is analogous to
     * { java.util.Optional#map Optional.map} and
     * { java.util.stream.Stream#map Stream.map}.
     * <p>
     * <p>See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param fn  the function to use to compute the value of the
     *            returned CompletionStage
     * @param <U> the function's return type
     * @return the new CompletionStage
     */
    <U> CompletionStep<U> thenApply(Function<? super T, ? extends U> fn);

    /**
     * Returns a new CompletionStage that, when this stage completes
     * normally, is executed using this stage's default asynchronous
     * execution facility, with this stage's result as the argument to
     * the supplied function.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param fn  the function to use to compute the value of the
     *            returned CompletionStage
     * @param <U> the function's return type
     * @return the new CompletionStage
     */
    <U> CompletionStep<U> thenApplyAsync
    (Function<? super T, ? extends U> fn);

    /**
     * Returns a new CompletionStage that, when this stage completes
     * normally, is executed using the supplied Executor, with this
     * stage's result as the argument to the supplied function.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param fn       the function to use to compute the value of the
     *                 returned CompletionStage
     * @param executor the executor to use for asynchronous execution
     * @param <U>      the function's return type
     * @return the new CompletionStage
     */
    <U> CompletionStep<U> thenApplyAsync
    (Function<? super T, ? extends U> fn,
     Executor executor);

    /**
     * Returns a new CompletionStage that, when this stage completes
     * normally, is executed with this stage's result as the argument
     * to the supplied action.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param action the action to perform before completing the
     *               returned CompletionStage
     * @return the new CompletionStage
     */
    CompletionStep<Void> thenAccept(Consumer<? super T> action);

    /**
     * Returns a new CompletionStage that, when this stage completes
     * normally, is executed using this stage's default asynchronous
     * execution facility, with this stage's result as the argument to
     * the supplied action.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param action the action to perform before completing the
     *               returned CompletionStage
     * @return the new CompletionStage
     */
    CompletionStep<Void> thenAcceptAsync(Consumer<? super T> action);

    /**
     * Returns a new CompletionStage that, when this stage completes
     * normally, is executed using the supplied Executor, with this
     * stage's result as the argument to the supplied action.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param action   the action to perform before completing the
     *                 returned CompletionStage
     * @param executor the executor to use for asynchronous execution
     * @return the new CompletionStage
     */
    CompletionStep<Void> thenAcceptAsync(Consumer<? super T> action,
                                         Executor executor);

    /**
     * Returns a new CompletionStage that, when this stage completes
     * normally, executes the given action.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param action the action to perform before completing the
     *               returned CompletionStage
     * @return the new CompletionStage
     */
    CompletionStep<Void> thenRun(Runnable action);

    /**
     * Returns a new CompletionStage that, when this stage completes
     * normally, executes the given action using this stage's default
     * asynchronous execution facility.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param action the action to perform before completing the
     *               returned CompletionStage
     * @return the new CompletionStage
     */
    CompletionStep<Void> thenRunAsync(Runnable action);

    /**
     * Returns a new CompletionStage that, when this stage completes
     * normally, executes the given action using the supplied Executor.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param action   the action to perform before completing the
     *                 returned CompletionStage
     * @param executor the executor to use for asynchronous execution
     * @return the new CompletionStage
     */
    CompletionStep<Void> thenRunAsync(Runnable action,
                                      Executor executor);

    /**
     * Returns a new CompletionStage that, when this and the other
     * given stage both complete normally, is executed with the two
     * results as arguments to the supplied function.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param other the other CompletionStage
     * @param fn    the function to use to compute the value of the
     *              returned CompletionStage
     * @param <U>   the type of the other CompletionStage's result
     * @param <V>   the function's return type
     * @return the new CompletionStage
     */
    <U, V> CompletionStep<V> thenCombine
    (CompletionStep<? extends U> other,
     Function2<? super T, ? super U, ? extends V> fn);

    /**
     * Returns a new CompletionStage that, when this and the other
     * given stage both complete normally, is executed using this
     * stage's default asynchronous execution facility, with the two
     * results as arguments to the supplied function.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param other the other CompletionStage
     * @param fn    the function to use to compute the value of the
     *              returned CompletionStage
     * @param <U>   the type of the other CompletionStage's result
     * @param <V>   the function's return type
     * @return the new CompletionStage
     */
    <U, V> CompletionStep<V> thenCombineAsync
    (CompletionStep<? extends U> other,
     Function2<? super T, ? super U, ? extends V> fn);

    /**
     * Returns a new CompletionStage that, when this and the other
     * given stage both complete normally, is executed using the
     * supplied executor, with the two results as arguments to the
     * supplied function.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param other    the other CompletionStage
     * @param fn       the function to use to compute the value of the
     *                 returned CompletionStage
     * @param executor the executor to use for asynchronous execution
     * @param <U>      the type of the other CompletionStage's result
     * @param <V>      the function's return type
     * @return the new CompletionStage
     */
    <U, V> CompletionStep<V> thenCombineAsync
    (CompletionStep<? extends U> other,
     Function2<? super T, ? super U, ? extends V> fn,
     Executor executor);

    /**
     * Returns a new CompletionStage that, when this and the other
     * given stage both complete normally, is executed with the two
     * results as arguments to the supplied action.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param other  the other CompletionStage
     * @param action the action to perform before completing the
     *               returned CompletionStage
     * @param <U>    the type of the other CompletionStage's result
     * @return the new CompletionStage
     */
    <U> CompletionStep<Void> thenAcceptBoth
    (CompletionStep<? extends U> other,
     Consumer2<? super T, ? super U> action);

    /**
     * Returns a new CompletionStage that, when this and the other
     * given stage both complete normally, is executed using this
     * stage's default asynchronous execution facility, with the two
     * results as arguments to the supplied action.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param other  the other CompletionStage
     * @param action the action to perform before completing the
     *               returned CompletionStage
     * @param <U>    the type of the other CompletionStage's result
     * @return the new CompletionStage
     */
    <U> CompletionStep<Void> thenAcceptBothAsync
    (CompletionStep<? extends U> other,
     Consumer2<? super T, ? super U> action);

    /**
     * Returns a new CompletionStage that, when this and the other
     * given stage both complete normally, is executed using the
     * supplied executor, with the two results as arguments to the
     * supplied action.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param other    the other CompletionStage
     * @param action   the action to perform before completing the
     *                 returned CompletionStage
     * @param executor the executor to use for asynchronous execution
     * @param <U>      the type of the other CompletionStage's result
     * @return the new CompletionStage
     */
    <U> CompletionStep<Void> thenAcceptBothAsync
    (CompletionStep<? extends U> other,
     Consumer2<? super T, ? super U> action,
     Executor executor);

    /**
     * Returns a new CompletionStage that, when this and the other
     * given stage both complete normally, executes the given action.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param other  the other CompletionStage
     * @param action the action to perform before completing the
     *               returned CompletionStage
     * @return the new CompletionStage
     */
    CompletionStep<Void> runAfterBoth(CompletionStep<?> other,
                                      Runnable action);

    /**
     * Returns a new CompletionStage that, when this and the other
     * given stage both complete normally, executes the given action
     * using this stage's default asynchronous execution facility.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param other  the other CompletionStage
     * @param action the action to perform before completing the
     *               returned CompletionStage
     * @return the new CompletionStage
     */
    CompletionStep<Void> runAfterBothAsync(CompletionStep<?> other,
                                           Runnable action);

    /**
     * Returns a new CompletionStage that, when this and the other
     * given stage both complete normally, executes the given action
     * using the supplied executor.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param other    the other CompletionStage
     * @param action   the action to perform before completing the
     *                 returned CompletionStage
     * @param executor the executor to use for asynchronous execution
     * @return the new CompletionStage
     */
    CompletionStep<Void> runAfterBothAsync(CompletionStep<?> other,
                                           Runnable action,
                                           Executor executor);

    /**
     * Returns a new CompletionStage that, when either this or the
     * other given stage complete normally, is executed with the
     * corresponding result as argument to the supplied function.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param other the other CompletionStage
     * @param fn    the function to use to compute the value of the
     *              returned CompletionStage
     * @param <U>   the function's return type
     * @return the new CompletionStage
     */
    <U> CompletionStep<U> applyToEither
    (CompletionStep<? extends T> other,
     Function<? super T, U> fn);

    /**
     * Returns a new CompletionStage that, when either this or the
     * other given stage complete normally, is executed using this
     * stage's default asynchronous execution facility, with the
     * corresponding result as argument to the supplied function.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param other the other CompletionStage
     * @param fn    the function to use to compute the value of the
     *              returned CompletionStage
     * @param <U>   the function's return type
     * @return the new CompletionStage
     */
    <U> CompletionStep<U> applyToEitherAsync
    (CompletionStep<? extends T> other,
     Function<? super T, U> fn);

    /**
     * Returns a new CompletionStage that, when either this or the
     * other given stage complete normally, is executed using the
     * supplied executor, with the corresponding result as argument to
     * the supplied function.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param other    the other CompletionStage
     * @param fn       the function to use to compute the value of the
     *                 returned CompletionStage
     * @param executor the executor to use for asynchronous execution
     * @param <U>      the function's return type
     * @return the new CompletionStage
     */
    <U> CompletionStep<U> applyToEitherAsync
    (CompletionStep<? extends T> other,
     Function<? super T, U> fn,
     Executor executor);

    /**
     * Returns a new CompletionStage that, when either this or the
     * other given stage complete normally, is executed with the
     * corresponding result as argument to the supplied action.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param other  the other CompletionStage
     * @param action the action to perform before completing the
     *               returned CompletionStage
     * @return the new CompletionStage
     */
    CompletionStep<Void> acceptEither
    (CompletionStep<? extends T> other,
     Consumer<? super T> action);

    /**
     * Returns a new CompletionStage that, when either this or the
     * other given stage complete normally, is executed using this
     * stage's default asynchronous execution facility, with the
     * corresponding result as argument to the supplied action.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param other  the other CompletionStage
     * @param action the action to perform before completing the
     *               returned CompletionStage
     * @return the new CompletionStage
     */
    CompletionStep<Void> acceptEitherAsync
    (CompletionStep<? extends T> other,
     Consumer<? super T> action);

    /**
     * Returns a new CompletionStage that, when either this or the
     * other given stage complete normally, is executed using the
     * supplied executor, with the corresponding result as argument to
     * the supplied action.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param other    the other CompletionStage
     * @param action   the action to perform before completing the
     *                 returned CompletionStage
     * @param executor the executor to use for asynchronous execution
     * @return the new CompletionStage
     */
    CompletionStep<Void> acceptEitherAsync
    (CompletionStep<? extends T> other,
     Consumer<? super T> action,
     Executor executor);

    /**
     * Returns a new CompletionStage that, when either this or the
     * other given stage complete normally, executes the given action.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param other  the other CompletionStage
     * @param action the action to perform before completing the
     *               returned CompletionStage
     * @return the new CompletionStage
     */
    CompletionStep<Void> runAfterEither(CompletionStep<?> other,
                                        Runnable action);

    /**
     * Returns a new CompletionStage that, when either this or the
     * other given stage complete normally, executes the given action
     * using this stage's default asynchronous execution facility.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param other  the other CompletionStage
     * @param action the action to perform before completing the
     *               returned CompletionStage
     * @return the new CompletionStage
     */
    CompletionStep<Void> runAfterEitherAsync
    (CompletionStep<?> other,
     Runnable action);

    /**
     * Returns a new CompletionStage that, when either this or the
     * other given stage complete normally, executes the given action
     * using the supplied executor.
     * <p>
     * See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param other    the other CompletionStage
     * @param action   the action to perform before completing the
     *                 returned CompletionStage
     * @param executor the executor to use for asynchronous execution
     * @return the new CompletionStage
     */
    CompletionStep<Void> runAfterEitherAsync
    (CompletionStep<?> other,
     Runnable action,
     Executor executor);

    /**
     * Returns a new CompletionStage that is completed with the same
     * value as the CompletionStage returned by the given function.
     * <p>
     * <p>When this stage completes normally, the given function is
     * invoked with this stage's result as the argument, returning
     * another CompletionStage.  When that stage completes normally,
     * the CompletionStage returned by this method is completed with
     * the same value.
     * <p>
     * <p>To ensure progress, the supplied function must arrange
     * eventual completion of its result.
     * <p>
     * <p>This method is analogous to
     * { java.util.Optional#flatMap Optional.flatMap} and
     * { java.util.stream.Stream#flatMap Stream.flatMap}.
     * <p>
     * <p>See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param fn  the function to use to compute another CompletionStage
     * @param <U> the type of the returned CompletionStage's result
     * @return the new CompletionStage
     */
    <U> CompletionStep<U> thenCompose
    (Function<? super T, ? extends CompletionStep<U>> fn);

    /**
     * Returns a new CompletionStage that is completed with the same
     * value as the CompletionStage returned by the given function,
     * executed using this stage's default asynchronous execution
     * facility.
     * <p>
     * <p>When this stage completes normally, the given function is
     * invoked with this stage's result as the argument, returning
     * another CompletionStage.  When that stage completes normally,
     * the CompletionStage returned by this method is completed with
     * the same value.
     * <p>
     * <p>To ensure progress, the supplied function must arrange
     * eventual completion of its result.
     * <p>
     * <p>See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param fn  the function to use to compute another CompletionStage
     * @param <U> the type of the returned CompletionStage's result
     * @return the new CompletionStage
     */
    <U> CompletionStep<U> thenComposeAsync
    (Function<? super T, ? extends CompletionStep<U>> fn);

    /**
     * Returns a new CompletionStage that is completed with the same
     * value as the CompletionStage returned by the given function,
     * executed using the supplied Executor.
     * <p>
     * <p>When this stage completes normally, the given function is
     * invoked with this stage's result as the argument, returning
     * another CompletionStage.  When that stage completes normally,
     * the CompletionStage returned by this method is completed with
     * the same value.
     * <p>
     * <p>To ensure progress, the supplied function must arrange
     * eventual completion of its result.
     * <p>
     * <p>See the { CompletionStep} documentation for rules
     * covering exceptional completion.
     *
     * @param fn       the function to use to compute another CompletionStage
     * @param executor the executor to use for asynchronous execution
     * @param <U>      the type of the returned CompletionStage's result
     * @return the new CompletionStage
     */
    <U> CompletionStep<U> thenComposeAsync
    (Function<? super T, ? extends CompletionStep<U>> fn,
     Executor executor);

    /**
     * Returns a new CompletionStage that, when this stage completes
     * either normally or exceptionally, is executed with this stage's
     * result and exception as arguments to the supplied function.
     * <p>
     * <p>When this stage is complete, the given function is invoked
     * with the result (or {@code null} if none) and the exception (or
     * {@code null} if none) of this stage as arguments, and the
     * function's result is used to complete the returned stage.
     *
     * @param fn  the function to use to compute the value of the
     *            returned CompletionStage
     * @param <U> the function's return type
     * @return the new CompletionStage
     */
    <U> CompletionStep<U> handle
    (Function2<? super T, Throwable, ? extends U> fn);

    /**
     * Returns a new CompletionStage that, when this stage completes
     * either normally or exceptionally, is executed using this stage's
     * default asynchronous execution facility, with this stage's
     * result and exception as arguments to the supplied function.
     * <p>
     * <p>When this stage is complete, the given function is invoked
     * with the result (or {@code null} if none) and the exception (or
     * {@code null} if none) of this stage as arguments, and the
     * function's result is used to complete the returned stage.
     *
     * @param fn  the function to use to compute the value of the
     *            returned CompletionStage
     * @param <U> the function's return type
     * @return the new CompletionStage
     */
    <U> CompletionStep<U> handleAsync
    (Function2<? super T, Throwable, ? extends U> fn);

    /**
     * Returns a new CompletionStage that, when this stage completes
     * either normally or exceptionally, is executed using the
     * supplied executor, with this stage's result and exception as
     * arguments to the supplied function.
     * <p>
     * <p>When this stage is complete, the given function is invoked
     * with the result (or {@code null} if none) and the exception (or
     * {@code null} if none) of this stage as arguments, and the
     * function's result is used to complete the returned stage.
     *
     * @param fn       the function to use to compute the value of the
     *                 returned CompletionStage
     * @param executor the executor to use for asynchronous execution
     * @param <U>      the function's return type
     * @return the new CompletionStage
     */
    <U> CompletionStep<U> handleAsync
    (Function2<? super T, Throwable, ? extends U> fn,
     Executor executor);

    /**
     * Returns a new CompletionStage with the same result or exception as
     * this stage, that executes the given action when this stage completes.
     * <p>
     * <p>When this stage is complete, the given action is invoked
     * with the result (or {@code null} if none) and the exception (or
     * {@code null} if none) of this stage as arguments.  The returned
     * stage is completed when the action returns.
     * <p>
     * <p>Unlike method { #handle handle},
     * this method is not designed to translate completion outcomes,
     * so the supplied action should not throw an exception. However,
     * if it does, the following rules apply: if this stage completed
     * normally but the supplied action throws an exception, then the
     * returned stage completes exceptionally with the supplied
     * action's exception. Or, if this stage completed exceptionally
     * and the supplied action throws an exception, then the returned
     * stage completes exceptionally with this stage's exception.
     *
     * @param action the action to perform
     * @return the new CompletionStage
     */
    CompletionStep<T> whenComplete
    (Consumer2<? super T, ? super Throwable> action);

    /**
     * Returns a new CompletionStage with the same result or exception as
     * this stage, that executes the given action using this stage's
     * default asynchronous execution facility when this stage completes.
     * <p>
     * <p>When this stage is complete, the given action is invoked with the
     * result (or {@code null} if none) and the exception (or {@code null}
     * if none) of this stage as arguments.  The returned stage is completed
     * when the action returns.
     * <p>
     * <p>Unlike method { #handleAsync(Function2) handleAsync},
     * this method is not designed to translate completion outcomes,
     * so the supplied action should not throw an exception. However,
     * if it does, the following rules apply: If this stage completed
     * normally but the supplied action throws an exception, then the
     * returned stage completes exceptionally with the supplied
     * action's exception. Or, if this stage completed exceptionally
     * and the supplied action throws an exception, then the returned
     * stage completes exceptionally with this stage's exception.
     *
     * @param action the action to perform
     * @return the new CompletionStage
     */
    CompletionStep<T> whenCompleteAsync
    (Consumer2<? super T, ? super Throwable> action);

    /**
     * Returns a new CompletionStage with the same result or exception as
     * this stage, that executes the given action using the supplied
     * Executor when this stage completes.
     * <p>
     * <p>When this stage is complete, the given action is invoked with the
     * result (or {@code null} if none) and the exception (or {@code null}
     * if none) of this stage as arguments.  The returned stage is completed
     * when the action returns.
     * <p>
     * <p>Unlike method { #handleAsync(Function2,Executor) handleAsync},
     * this method is not designed to translate completion outcomes,
     * so the supplied action should not throw an exception. However,
     * if it does, the following rules apply: If this stage completed
     * normally but the supplied action throws an exception, then the
     * returned stage completes exceptionally with the supplied
     * action's exception. Or, if this stage completed exceptionally
     * and the supplied action throws an exception, then the returned
     * stage completes exceptionally with this stage's exception.
     *
     * @param action   the action to perform
     * @param executor the executor to use for asynchronous execution
     * @return the new CompletionStage
     */
    CompletionStep<T> whenCompleteAsync
    (Consumer2<? super T, ? super Throwable> action,
     Executor executor);

    /**
     * Returns a new CompletionStage that, when this stage completes
     * exceptionally, is executed with this stage's exception as the
     * argument to the supplied function.  Otherwise, if this stage
     * completes normally, then the returned stage also completes
     * normally with the same value.
     *
     * @param fn the function to use to compute the value of the
     *           returned CompletionStage if this CompletionStage completed
     *           exceptionally
     * @return the new CompletionStage
     */
    CompletionStep<T> exceptionally
    (Function<Throwable, ? extends T> fn);

    /**
     * Returns a { CompletableFuture} maintaining the same
     * completion properties as this stage. If this stage is already a
     * CompletableFuture, this method may return this stage itself.
     * Otherwise, invocation of this method may be equivalent in
     * effect to {@code thenApply(x -> x)}, but returning an instance
     * of type {@code CompletableFuture}.
     *
     * @return the CompletableFuture
     */
    CompletableFuture<T> toCompletableFuture();

}
