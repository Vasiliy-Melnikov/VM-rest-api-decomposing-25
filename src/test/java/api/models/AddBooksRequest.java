package api.models;

import lombok.Data;
import java.util.List;

@Data
public class AddBooksRequest {
    private String userId;
    private List<IsbnModel> collectionOfIsbns;
}
