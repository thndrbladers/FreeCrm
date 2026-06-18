package com.freecrm.pojo;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Company {
    private String id;
    @JsonProperty("account_id")
    private String accountId;
    @JsonProperty("template_id")
    private String templateId;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("last_modified")
    private String lastModified;
    @JsonProperty("created_by")
    private User createdBy;
    @JsonProperty("aux_id")
    private String auxId;
    @JsonProperty("aux_source")
    private String auxSource;
    @JsonProperty("aux_source_name")
    private String auxSourceName;
    @JsonProperty("private")
    private boolean isPrivate;
    private List<Object> acl;
    private List<String> tags;
    private int rating;
    private Flags flags;
    @JsonProperty("_uri")
    private String uri;
    @JsonProperty("_type")
    private String type;
    private String name;
    private String description;
    private String url;
    private String image;
    private List<Object> channels;
    private List<Address> addresses;
    private Access access;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getAuxId() {
        return auxId;
    }

    public void setAuxId(String auxId) {
        this.auxId = auxId;
    }

    public String getAuxSource() {
        return auxSource;
    }

    public void setAuxSource(String auxSource) {
        this.auxSource = auxSource;
    }

    public String getAuxSourceName() {
        return auxSourceName;
    }

    public void setAuxSourceName(String auxSourceName) {
        this.auxSourceName = auxSourceName;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public List<Object> getAcl() {
        return acl;
    }

    public void setAcl(List<Object> acl) {
        this.acl = acl;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Flags getFlags() {
        return flags;
    }

    public void setFlags(Flags flags) {
        this.flags = flags;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Object> getChannels() {
        return channels;
    }

    public void setChannels(List<Object> channels) {
        this.channels = channels;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

	@Override
	public String toString() {
		return "Company [id=" + id + ", accountId=" + accountId + ", templateId=" + templateId + ", createdAt="
				+ createdAt + ", lastModified=" + lastModified + ", createdBy=" + createdBy + ", auxId=" + auxId
				+ ", auxSource=" + auxSource + ", auxSourceName=" + auxSourceName + ", isPrivate=" + isPrivate
				+ ", acl=" + acl + ", tags=" + tags + ", rating=" + rating + ", flags=" + flags + ", uri=" + uri
				+ ", type=" + type + ", name=" + name + ", description=" + description + ", url=" + url + ", image="
				+ image + ", channels=" + channels + ", addresses=" + addresses + ", access=" + access + "]";
	}

  
}