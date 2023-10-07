package yi.shi.plinth.http;

/**
 * @author yshi
 *
 */
public enum MimeType {
	/**
	 * 
	 * https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Complete_list_of_MIME_types
	 * */
	
	ALL("*/*"),
	
	TEXT_PLAIN("text/plain"),
	TEXT_HTML("text/html"),
	TEXT_CSS("text/css"),
	TEXT_CSV("text/csv"),
	TEXT_XML("text/xml"),
	
	APPLICATION("application/*"),
	APPLICATION_JAVASCRIPT("application/javascript"),
	APPLICATION_ECMASCRIPT("application/ecmascript"),
	APPLICATION_JSON("application/json"),
	APPLICATION_XML("application/xml"),
	APPLICATION_OCTET_STREAM("application/octet-stream"),
	
	APPLICATION_PDF("application/pdf"),
	APPLICATION_DOC("application/msword"),
	APPLICATION_DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
	APPLICATION_PPT("application/vnd.ms-powerpoint"),
	APPLICATION_PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
	APPLICATION_XLS("application/vnd.ms-excel"),
	APPLICATION_XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
	
	APPLICATION_ZIP("application/zip"),
	APPLICATION_GZIP("application/gzip"),
	APPLICATION_RAR("application/vnd.rar"),
	APPLICATION_JAR("application/java-archive"),
	APPLICATION_TAR("application/x-tar"),
	APPLICATION_7Z("application/x-7z-compressed"),
	
	APPLICATION_SHELL("application/x-sh"),
	
	IMAGE_JPEG("image/jpeg"),
	IMAGE_PNG("image/png"),
	IMAGE_GIF("image/gif"),
	IMAGE_BMP("image/bmp"),
	IMAGE_ICON("image/vnd.microsoft.icon"),
	IMAGE_SVG("image/svg+xml"),
	IMAGE_TIFF("image/tiff"),
	IMAGE_WEBP("image/webp"),
	
	VIDEO_MP4("video/mp4"),
	VIDEO_AVI("video/x-msvideo"),
	VIDEO_MPEG("video/mpeg"),
	VEDIO_WEBM("video/webm"),
	
	AUDIO_WEBM("audio/webm"),
	AUDIO_MP3("audio/mpeg"),
	AUDIO_WAV("audio/wav");
	
	String type;
	
	private MimeType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
