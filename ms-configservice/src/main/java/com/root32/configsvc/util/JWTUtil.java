package  com.root32.configsvc.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.root32.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {
	// minutes * seconds
	private static final int JWT_TOKEN_VALIDITY = 525960 * 60;
	private byte[] secretKey = "JWT_SECRET".getBytes();

//	public String generateToken(User user) {
//		Map<String, Object> claims = new HashMap<>();
//		return doGenerateToken(claims, user.getEmailId());
//	}
//
//	private String doGenerateToken(Map<String, Object> claims, String subject) {
//		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date())
//				.setExpiration(new Date(System.currentTimeMillis() + (JWT_TOKEN_VALIDITY * 1000)))
//				.signWith(SignatureAlgorithm.HS512, secretKey).compact();
//	}

	public String generateToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("sub", user.getEmailId());
		claims.put("sub2", user.getContactNumber());
		return doGenerateToken(claims);
	}

	private String doGenerateToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + (JWT_TOKEN_VALIDITY * 1000)))
				.signWith(SignatureAlgorithm.HS512, secretKey).compact();
	}

	public Boolean validateToken(String token, User user) {
		final String userName = getUserNameFromToken(token);
		if (userName == null || userName.trim().equals("")) {
			throw new IllegalArgumentException("Invalid Token");
		}
		return (userName.equals(userName) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public String getUserNameFromToken(String token) {
		return getClaimsFromToken(token, Claims::getSubject);
	}

	private Date getExpirationDateFromToken(String token) {
		return getClaimsFromToken(token, Claims::getExpiration);
	}

	private <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

}
