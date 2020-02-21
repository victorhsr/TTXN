package io.github.victorhsr.sample.person;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterPersonDTO {

    @JsonProperty("tenant")
    private String tenant;

    @JsonProperty("full_name")
    private String fullName;

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String tenant;
        private String fullName;

        private Builder() {
        }

        public Builder tenant(String tenant) {
            this.tenant = tenant;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public RegisterPersonDTO build() {
            RegisterPersonDTO registerPersonDTO = new RegisterPersonDTO();
            registerPersonDTO.setTenant(tenant);
            registerPersonDTO.setFullName(fullName);
            return registerPersonDTO;
        }
    }
}
