package app.quantun.auth.config.security;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MicrosoftGroup {

    private String id;
    private String displayName;
    private String description;
}

