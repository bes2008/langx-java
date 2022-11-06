package com.jn.langx.text.html;


import com.jn.langx.util.escape.Escaper;
import com.jn.langx.util.escape.Escapers;

/**
 * {@code Escaper} instances suitable for strings to be included in HTML attribute values and
 * <em>most</em> elements' text contents. When possible, avoid manual escaping by using templating
 * systems and high-level APIs that provide autoescaping.
 * One Google-authored templating system available for external use is <a
 * href="https://developers.google.com/closure/templates/">Closure Templates</a>.
 * <p>
 * <p>HTML escaping is particularly tricky: For example, <a href="http://goo.gl/5TgZb">some
 * elements' text contents must not be HTML escaped</a>. As a result, it is impossible to escape an
 * HTML document correctly without domain-specific knowledge beyond what {@code HtmlEscapers}
 * provides. We strongly encourage the use of HTML templating systems.
 *
 */
public final class HtmlEscapers {
    /**
     * Returns an {@link Escaper} instance that escapes HTML metacharacters as specified by <a
     * href="http://www.w3.org/TR/html4/">HTML 4.01</a>. The resulting strings can be used both in
     * attribute values and in <em>most</em> elements' text contents, provided that the HTML
     * document's character encoding can encode any non-ASCII code points in the input (as UTF-8 and
     * other Unicode encodings can).
     * <p>
     * <p><b>Note:</b> This escaper only performs minimal escaping to make content structurally
     * compatible with HTML. Specifically, it does not perform entity replacement (symbolic or
     * numeric), so it does not replace non-ASCII code points with character references. This escaper
     * escapes only the following five ASCII characters: {@code '"&<>}.
     */
    public static Escaper htmlEscaper() {
        return HTML_ESCAPER;
    }

    // For each xxxEscaper() method, please add links to external reference pages
    // that are considered authoritative for the behavior of that escaper.

    private static final Escaper HTML_ESCAPER =
            Escapers.builder()
                    .addEscape('"', "&quot;")
                    // Note: "&apos;" is not defined in HTML 4.01.
                    .addEscape('\'', "&#39;")
                    .addEscape('&', "&amp;")
                    .addEscape('<', "&lt;")
                    .addEscape('>', "&gt;")
                    .build();

    private HtmlEscapers() {
    }
}
