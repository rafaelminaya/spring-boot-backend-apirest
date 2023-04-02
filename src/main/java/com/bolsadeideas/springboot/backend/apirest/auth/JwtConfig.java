package com.bolsadeideas.springboot.backend.apirest.auth;

public class JwtConfig {
	public static final String LLAVE_SECRETA = "alguna.clave.secreta.12345678";

	// El profesor generó estas RSA por un software, nosotros por una web.
	public static String RSA_PRIVATE = "-----BEGIN RSA PRIVATE KEY-----\r\n"
			+ "MIICWgIBAAKBgEtvM8X9fIWUuELgUu6+Mh1D/UNGkqVNw0VMUUbnrL6/4Qzv5x2z\r\n"
			+ "2YJ5FUI52UrI/du/3z80R+7bmnoXXmrfb5OcpJJxZ6GCdRV936QXuaH5pm0ma5+7\r\n"
			+ "s1Nl7ozc3x+A4Y4F0KimHUbKxgqsE3VPsy4yJ4ErgC6s220/pK/EJ+fHAgMBAAEC\r\n"
			+ "gYAIcQtULsfOkxQDt+LQAMzO5Zrj+nPSt98QVDgMPUThm8ttjTNJ6aR/q1krrhmY\r\n"
			+ "+uEdxF0RT6J1InIpE5BnWVW9CfyUbLhRyoC1RGKDH2gtTN28CEimhsALl+m6FozO\r\n"
			+ "zyUcQeCN8fniycdddMfoBVmvlj2Vi7fcyBiRSx96L6BPEQJBAJBT0N6GRNOXpQn2\r\n"
			+ "TeK6Ue8xzG0TKGiBAR8RWTybsqaqaTBrZk0wqhD+oeUSSWu9UkWC/EXAtWuztlR/\r\n"
			+ "U97wBUMCQQCFzSV/udSpMVxyzVwBFO39FdyfNkzCvGWvmwUfJmflvLb8Lr4JEL+A\r\n"
			+ "8l7KB1tRk0CGm0+5pcP0YoR0lBrKz+ktAkB06XBo0ZsvS8gFKeAtqYB1ooFWWg6B\r\n"
			+ "mP6vi/4deoJdP+21q0pTfPhGi/3Y8ddHzzS12kVAu7o6ZHluOTZXgVbBAkAExfbK\r\n"
			+ "gbSmfI5RwiXAHCEJYFhIjcVbRSFTjHI4VERsm7jjcj9xT1mlnf/nsS9+z4QDyVKk\r\n"
			+ "ytccxs724bm69oPZAkA/IFP7KqhCKTOfLRNfZlipzYVTBg17vaWDzlFT0CMLgK90\r\n"
			+ "pCcYN96YrgclZ+vtpvsxPiNYXRHa9p+ROOVEiJB8\r\n"
			+ "-----END RSA PRIVATE KEY-----";
	
	public static String RSA_PUBLIC = "-----BEGIN PUBLIC KEY-----\r\n"
			+ "MIGeMA0GCSqGSIb3DQEBAQUAA4GMADCBiAKBgEtvM8X9fIWUuELgUu6+Mh1D/UNG\r\n"
			+ "kqVNw0VMUUbnrL6/4Qzv5x2z2YJ5FUI52UrI/du/3z80R+7bmnoXXmrfb5OcpJJx\r\n"
			+ "Z6GCdRV936QXuaH5pm0ma5+7s1Nl7ozc3x+A4Y4F0KimHUbKxgqsE3VPsy4yJ4Er\r\n"
			+ "gC6s220/pK/EJ+fHAgMBAAE=\r\n"
			+ "-----END PUBLIC KEY-----";
}
