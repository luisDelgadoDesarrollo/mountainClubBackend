package luis.delgado.clubmontana.backend.domain.model;

public record MailAttachment(String fileName, byte[] content, String contentType) {}
