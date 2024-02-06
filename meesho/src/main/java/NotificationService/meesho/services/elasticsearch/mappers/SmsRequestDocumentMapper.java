package NotificationService.meesho.services.elasticsearch.mappers;

import NotificationService.meesho.dao.entities.sql.SmsRequest;
import NotificationService.meesho.dao.entities.sql.SmsRequestsDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SmsRequestDocumentMapper {
    SmsRequestDocumentMapper mapper = Mappers.getMapper(SmsRequestDocumentMapper.class);

    SmsRequestsDocument convertToDocument(SmsRequest smsRequest);

}