package eu.arrowhead.common.dto.internal;

import java.io.Serializable;
import java.util.List;

public class CertificateSigningResponseDTO implements Serializable {

    private static final long serialVersionUID = -6810780579000655432L;

    private List<String> certificateChain;

    public CertificateSigningResponseDTO() {
    }

    public CertificateSigningResponseDTO(List<String> certificateChain) {
        this.certificateChain = certificateChain;
    }

    public List<String> getCertificateChain() {
        return certificateChain;
    }

    public void setCertificateChain(List<String> certificateChain) {
        this.certificateChain = certificateChain;
    }
}
