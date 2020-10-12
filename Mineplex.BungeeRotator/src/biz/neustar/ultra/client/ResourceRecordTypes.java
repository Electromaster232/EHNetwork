package biz.neustar.ultra.client;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


/*
 * User: jbodner
 * Date: 1/28/13
 * Time: 12:32 PM
 *
 * Copyright 2000-2013 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */
public enum ResourceRecordTypes {
	
	ALL_TYPES(0),
    A(1),
    NS(2),
    MD(3),
    MF(4),
    CNAME(5),
    SOA(6),
    MB(7),
    MG(8),
    MR(9),
    NULL(10),
    WKS(11),
    PTR(12),
    HINFO(13),
    MINFO(14),
    MX(15),
    TXT(16),
    RP(17),
    AFSDB(18),
    X25(19),
    ISDN(20),
    RT(21),
    NSAP(22),
    NSAP_PTR(23),
    SIG (24),
    KEY  (25),
    PX (26),
    CPOS( 27),
    AAAA ( 28),
    LOC(29),
    NXT(30),
    EID( 31),
    NIMLOC( 32),
    SRV (33),
    NAPTR(35),
    DS (43),
    SSHFP  (44),
    RRSIG (46),
    NSEC  (47),
    DNSKEY (48),
    NSEC3 (50),
    NSEC3PARAM (51),
    SPF(99),
    UINFO(100),
    UID (101),
    GID (102),
    AXFR ( 252),
    MAILA (253),
    MAILB (254),
    ANY (255),
    NORESPONSE (902),
    DLV  (32769),
	SB (65280);
	
	private static final Map<Integer,ResourceRecordTypes> lookup   = new HashMap<Integer,ResourceRecordTypes>();
	private static final Map<ResourceRecordTypes, Integer> reverseLookup = new HashMap<ResourceRecordTypes,Integer>();

	static {
	    for(ResourceRecordTypes r : EnumSet.allOf(ResourceRecordTypes.class)){
	         lookup.put(r.recType(), r);
	    	 reverseLookup.put(r, r.recType());
	    }
	}
  
   	private int recType;
	
	/**
	 * @param code
	 * @param message
	 */
	ResourceRecordTypes(int code) {
		this.recType = code;		
	}

	/**
	 * 
	 * @return code
	 */
	public int recType() {
		return recType;
	}

	 public static ResourceRecordTypes get(int code) { 
         return lookup.get(code); 
    }
	
	 public static Integer get(ResourceRecordTypes rec){
		 return reverseLookup.get(rec);
	 }
	 
	 public boolean equals(int code){
		 return this.equals(lookup.get(code));
	 }
}
