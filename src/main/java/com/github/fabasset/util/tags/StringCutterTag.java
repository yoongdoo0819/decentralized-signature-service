package com.github.fabasset.util.tags;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * string cutter custom tag
 */
@Getter
@Setter
public class StringCutterTag extends BodyTagSupport {

	private static final long serialVersionUID = -1946662271953931844L;
	
	private String title;
	
	private String suffix;
	
	private int length;

	private boolean escapeXml;

	public int doStartTag() throws JspTagException {

		try {
			JspWriter out = pageContext.getOut();

			String replaceTitle = "";
			if (StringUtils.isNotEmpty(title)) {
				if (isEscapeXml()) {
					title = StringUtils.replace(title, "&", "&amp;");
					title = StringUtils.replace(title, "<", "&lt;");
					title = StringUtils.replace(title, ">", "&gt;");
				}

				if (title.length()>length) {
					replaceTitle = StringUtils.substring(title, 0, length) + suffix;
				} else {
					replaceTitle = title;
				}
			} else {
				title = "&nbsp;";
			}

			out.print(replaceTitle);

		} catch (Exception e) {
			throw new JspTagException(e.toString());
		}
		return SKIP_BODY;
	}
}