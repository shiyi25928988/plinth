package yi.shi.plinth.http.result;

import yi.shi.plinth.http.MimeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author shiyi
 *
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HTML implements ReturnType<InputStream>{

	private InputStream htmlContent;
	private String url;

	private final MimeType mimeType = MimeType.TEXT_HTML;

	public HTML setUrl(String url) throws IOException {
		this.url = url;
		htmlContent = Object.class.getClass().getResourceAsStream(url);
		return this;
	}

	public HTML setHtmlContent(String htmlContent) {
		this.htmlContent = IOUtils.toInputStream(htmlContent);
		return this;
	}
	
	@Override
	public InputStream getData() {
		return htmlContent;
	}

}
