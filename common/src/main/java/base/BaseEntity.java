package base;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BaseEntity {
	@CreatedBy
	@Column(name = "created_by", updatable = false)
	private String createdBy;

	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedBy
	@Column(name = "modified_by")
	private String modifiedBy;

	@LastModifiedDate
	@Column(name = "modified_at")
	private LocalDateTime modifiedAt;

	@Setter
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Column(name = "is_deleted")
	private Boolean isDeleted = Boolean.FALSE;

	public void softDelete() {
		this.isDeleted = Boolean.TRUE;
		this.deletedAt = LocalDateTime.now();
	}

}
