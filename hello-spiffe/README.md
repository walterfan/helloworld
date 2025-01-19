X.509 是一种通用的数字证书标准，广泛用于 TLS/SSL 协议、数字签名、加密通信等场景。它定义了数字证书的格式和验证方法。

---

## **X.509 证书的结构**

一个 X.509 证书的核心内容包含以下部分：

### **1. 版本 (Version)**
- 指明证书使用的 X.509 标准版本（通常为 v3）。
- 当前广泛使用的是 v3，支持扩展字段。

### **2. 序列号 (Serial Number)**
- 证书颁发机构 (CA) 分配的唯一标识，用于区分不同的证书。

### **3. 签名算法 (Signature Algorithm)**
- 表明证书签名使用的算法，如 `SHA256-RSA`。
- 此字段同时指示如何验证证书的合法性。

### **4. 颁发者 (Issuer)**
- 表示签发此证书的证书颁发机构 (CA) 的信息（如国家、机构名等）。
- 这是一个 X.500 格式的名称，如：
  ```
  C=CN, O=Example CA, CN=Example Root CA
  ```

### **5. 有效期 (Validity)**
- 包括两个字段：
  - `Not Before`: 证书开始生效的时间。
  - `Not After`: 证书到期的时间。

### **6. 使用者 (Subject)**
- 表示此证书的持有者（如域名、组织名称等）。
- 格式与 `Issuer` 类似，例如：
  ```
  C=CN, O=Example Organization, CN=www.example.com
  ```

### **7. 使用者公钥 (Subject Public Key Info)**
- 包含公钥的类型（如 RSA、ECDSA）和公钥数据。
- 用于加密和签名验证。

### **8. 扩展字段 (Extensions)**
- 提供额外的功能和信息（v3 特有）。
  - **Key Usage**: 指明证书用途（如数字签名、密钥加密）。
  - **Extended Key Usage (EKU)**: 指明更具体的用途（如 HTTPS 服务器、代码签名）。
  - **Subject Alternative Name (SAN)**: 支持多个域名或 IP 地址。
  - **CRL Distribution Points**: 指定证书吊销列表 (CRL) 的 URL。

### **9. 签名 (Signature)**
- 由 CA 使用其私钥对上述内容签名，确保数据完整性和来源可信性。

---

## **X.509 证书的签发过程**

1. **申请者生成密钥对 (Key Pair Generation)**
   - 用户生成一对密钥（公钥和私钥）。
   - 公钥会包含在证书中，私钥由用户自行保管。

2. **提交 CSR (Certificate Signing Request)**
   - 用户创建一个证书签名请求 (CSR)，包含以下信息：
     - 公钥
     - 使用者信息（如域名、组织）
     - 签名（由用户私钥签名）
   - CSR 提交给证书颁发机构 (CA)。

3. **CA 验证申请者身份**
   - CA 验证用户提供的信息是否属实（如验证域名的所有权）。

4. **签发证书**
   - CA 生成证书，包含申请者的公钥及其他信息。
   - 使用 CA 的私钥对证书内容签名。

5. **分发证书**
   - 签发后的证书交给用户，用于安全通信或身份验证。

---

## **X.509 证书的验证过程**

1. **验证证书链 (Certificate Chain)**
   - 检查证书是否由可信的根 CA 或中间 CA 签发。
   - 验证链条中的所有证书都有效且未过期。

2. **验证签名**
   - 使用颁发者（Issuer）字段对应 CA 的公钥，验证签名字段。
   - 确保证书内容未被篡改。

3. **检查有效期**
   - 确保证书的当前时间在有效期范围内。

4. **检查吊销状态**
   - 查询 CRL 或 OCSP，看证书是否被吊销。

5. **验证扩展字段**
   - 检查扩展字段是否符合期望用途（如服务器证书必须包含 `Extended Key Usage` 的 TLS 标记）。

---

## **可视化理解 X.509 证书**

### **树形结构**
```
X.509 Certificate
├── Version: v3
├── Serial Number: 123456789
├── Signature Algorithm: SHA256-RSA
├── Issuer: C=CN, O=Example CA, CN=Root CA
├── Validity
│   ├── Not Before: 2025-01-01
│   ├── Not After:  2026-01-01
├── Subject: C=CN, O=Example Org, CN=www.example.com
├── Public Key Info
│   ├── Algorithm: RSA
│   ├── Public Key: <Key Data>
├── Extensions
│   ├── Key Usage: Digital Signature, Key Encipherment
│   ├── Subject Alternative Name: www.example.com, example.com
│   ├── CRL Distribution Points: http://crl.example.com
└── Signature: <Signature Data>
```

