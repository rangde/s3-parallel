package org.lis.aws.s3.helper;

public abstract class AuditAware
{
	protected AuditUtil auditUtil;
	
	public void setAuditUtil(AuditUtil auditUtil) {
		this.auditUtil = auditUtil;
	}
}
