
# JWT Abstract - Simplified JWT Token Handling for Spring Boot

[![Maven Central](https://img.shields.io/maven-central/v/com.sandeepprabhakula/jwt-abstract)](https://search.maven.org/artifact/com.sandeepprabhakula/jwt-abstract)

## 🚀 Overview

**JWT Abstract** is a productivity-boosting utility for Spring Boot developers that eliminates repetitive boilerplate code for creating and parsing JWT (JSON Web Tokens). 

This library provides an abstraction over common operations such as generating tokens and extracting claims — so you can focus more on building business logic than rewriting utility classes.

---

## 📦 Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
  <groupId>com.sandeepprabhakula</groupId>
  <artifactId>jwt-abstract</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

Then install dependencies:

```bash
mvn install
```

---

## ❌ The Problem

Tired of writing the same JWT utility class over and over again?

```java
public class TokenGenerator {
    public String generateToken(String SECRET,String userName,Map<String,Object>claims,long expirationDuration){
        return createToken(SECRET,claims,userName,expirationDuration);
    }

    private String createToken(String SECRET, Map<String, Object> claims, String userName,long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSignKey(SECRET), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey(String SECRET) {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Date extractExpiration(String SECRET,String token) {
        return extractClaim(SECRET,token, Claims::getExpiration);
    }

    private <T> T extractClaim(String SECRET, String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(SECRET, token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String SECRET,String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey(SECRET))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String SECRET,String token) {
        return extractExpiration(SECRET,token).before(new Date());
    }
}
```

---

## ✅ The Solution

Replace all that with a simple and clean abstraction:

### 🔐 Generate JWT Token

```java
Map<String, Object> claims = new HashMap<>();
claims.put("email", "sandeepprabhakula10@gmail.com");
claims.put("role", "ROLE_ADMIN");

long tokenExpiryInMillis = 1000 * 60 * 5; // 5 minutes

// Generate Token
String token = tokenGenerator.generateToken(SECRET, "SandeepPrabhakula", claims, tokenExpiryInMillis);
System.out.println(token);
```

---

### 📤 Extract Claims from Token

```java
Map<String, Object> extractedClaims = tokenGenerator.extractAllClaims(SECRET, token);
for (String key : extractedClaims.keySet()) {
    System.out.println("Key: " + key + ", Value: " + extractedClaims.get(key));
}
```

---

## 🧠 Features

- 🔑 Easy JWT Token generation with custom claims
- 🔍 Simple claim extraction
- ⛔ No more boilerplate utility classes
- 🧩 Plug & play with Spring Boot applications
- 🔒 Supports secure HMAC SHA-256 signing

---

## 🛠️ Tech Stack

- Java 17+
- Spring Boot
- JJWT (Java JWT library)
- Maven

---

## 🧾 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Sandeep Prabhakula**  
[GitHub](https://github.com/sandeepprabhakula) • [LinkedIn](https://www.linkedin.com/in/sandeepprabhakula)

---

> 🚀 Save time. Avoid boilerplate. Let `jwt-abstract` handle your JWT needs.
