package yi.shi.plinth.http.result;

import yi.shi.plinth.http.MimeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BINARY implements ReturnType<InputStream> {

    private InputStream inputStream;

    private MimeType mimeType = MimeType.ALL;

    public BINARY setData(InputStream inputStream) {
        this.inputStream = inputStream;
        return this;
    }

    public BINARY setMimeType(MimeType mimeType){
        this.mimeType = mimeType;
        return this;
    }

    @Override
    public MimeType getMimeType() {
        return this.mimeType;
    }

    @Override
    public InputStream getData() {
        return this.inputStream;
    }
}
