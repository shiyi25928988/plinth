package yi.shi.plinth.auth.model;

import lombok.Data;

import java.util.List;

@Data
public class User {

    String username;

    List<String> roleList;
}
