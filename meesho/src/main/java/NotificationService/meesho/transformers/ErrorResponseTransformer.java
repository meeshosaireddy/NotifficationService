package NotificationService.meesho.transformers;

import NotificationService.meesho.dao.entities.models.ErrorResponse;

public class ErrorResponseTransformer {
    public static ErrorResponse errorResponse(String code,String message){
        ErrorResponse.ErrorDetails errorDetails = new ErrorResponse.ErrorDetails(code,message);
        ErrorResponse errorResponse=new ErrorResponse(errorDetails);
        return errorResponse;
    }
}
