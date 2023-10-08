package yi.shi.plinth.http.result;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorMessage {

    String errMsg;
    int code;
}
