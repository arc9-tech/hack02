package tech.arc9.gateway.model;

import tech.arc9.mediaservice.MediaServiceProto;

import java.time.Instant;

public class SignedUrl {
    private String url;
    private Instant expiredAt;

    public SignedUrl(MediaServiceProto.Response proto) {
        setUrl(proto.getSasUrl());
        setExpiredAt(Instant.ofEpochMilli(proto.getExpiredAt()));
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Instant getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Instant expiredAt) {
        this.expiredAt = expiredAt;
    }
}
