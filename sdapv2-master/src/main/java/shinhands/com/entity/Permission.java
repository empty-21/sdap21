package shinhands.com.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
	private Boolean viewhost = false;
	private Boolean mgrhost = false;
	private Boolean viewconso = false;
	private Boolean mgrconso = false;
	private Boolean viewnetwork = false;
	private Boolean mgrnetwork = false;
	private Boolean menumgrda = false;
	private Boolean viewissue = false;
	private Boolean mgrissue = false;
	private Boolean viewcontract = false;
	private Boolean viewtransfer = false;
	private Boolean menumgrpolicy = false;
	private Boolean viewbacicpolicy = false;
	private Boolean mgrbasicpolicy = false;
	private Boolean viewauthgrp = false;
	private Boolean mgrauthgrp = false;
	private Boolean viewmember = false;
	private Boolean mgrmember = false;
	private Boolean viewkyc = false;
	private Boolean mgrkyc = false;
	private Boolean viewnotice = false;
	private Boolean mgrnotice = false;
	private Boolean viewnotify = false;
	private Boolean mgrnotify = false;
	private Boolean viewauditlog = false;
	private Boolean menublockexplorer = false;
}
