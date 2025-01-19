import socket
import ssl
from cryptography import x509
from cryptography.x509.oid import NameOID
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.asymmetric import rsa
from cryptography.hazmat.primitives.serialization import Encoding, NoEncryption, PrivateFormat
from cryptography.hazmat.backends import default_backend
from datetime import datetime, timedelta, timezone


def download_certificate(hostname, port=443):
    context = ssl.create_default_context()

    with socket.create_connection((hostname, port)) as sock:
        with context.wrap_socket(sock, server_hostname=hostname) as ssl_sock:
            der_cert = ssl_sock.getpeercert(binary_form=True)  # get DER format certificate
            cert = x509.load_der_x509_certificate(der_cert)  # convert DER format certificate to X.509
            return cert

def write_certificate(certificate, output_path):
    if not output_path.endswith(".pem"):
        output_path = output_path + ".pem"
    with open(output_path, "wb") as cert_file:
        cert_file.write(certificate.public_bytes(Encoding.PEM))

def generate_svid(spiffe_id, output_path="svid"):
    # 1. Generate a private key
    private_key = rsa.generate_private_key(
        public_exponent=65537,
        key_size=2048,
    )

    # 2. Create a certificate,  subject == issuer for self-signed certificate
    subject = issuer = x509.Name([
        x509.NameAttribute(NameOID.COUNTRY_NAME, u"CN"),
        x509.NameAttribute(NameOID.STATE_OR_PROVINCE_NAME, u"Anhui"),
        x509.NameAttribute(NameOID.LOCALITY_NAME, u"Hefei"),
        x509.NameAttribute(NameOID.ORGANIZATION_NAME, u"fanyamin.com"),
        x509.NameAttribute(NameOID.COMMON_NAME, u"hello-spiffe"),
    ])

    # SPIFFE ID in the SAN field
    san = x509.SubjectAlternativeName([
        x509.UniformResourceIdentifier(spiffe_id),
    ])

    certificate = (
        x509.CertificateBuilder()
        .subject_name(subject)
        .issuer_name(issuer)
        .public_key(private_key.public_key())
        .serial_number(x509.random_serial_number())
        .not_valid_before(datetime.now(timezone.utc))
        .not_valid_after(
            datetime.now(timezone.utc) + timedelta(days=365)  # Valid for 1 year
        )
        .add_extension(san, critical=False)
        .sign(private_key, hashes.SHA256())
    )

    # 3. Save the private key and certificate to files
    with open(f"{output_path}_key.pem", "wb") as key_file:
        key_file.write(
            private_key.private_bytes(
                encoding=Encoding.PEM,
                format=PrivateFormat.TraditionalOpenSSL,
                encryption_algorithm=NoEncryption(),
            )
        )

    with open(f"{output_path}_cert.pem", "wb") as cert_file:
        cert_file.write(certificate.public_bytes(Encoding.PEM))

    print(f"SVID generated with SPIFFE ID: {spiffe_id}")
    print(f"Private Key: {output_path}_key.pem")
    print(f"Certificate: {output_path}_cert.pem")
    return f"{output_path}_cert.pem"


def parse_certificate(cert_path):
    # Read the certificate
    with open(cert_path, "rb") as cert_file:
        cert_data = cert_file.read()

    # Load the certificate
    certificate = x509.load_pem_x509_certificate(cert_data, default_backend())

    # Display parsed information
    print(f"{'-'*20} X.509 Certificate Details {'-'*20}\n")

    # Subject
    print("Subject:")
    for attribute in certificate.subject:
        print(f"  {attribute.oid._name}: {attribute.value}")

    print("\nIssuer:")
    for attribute in certificate.issuer:
        print(f"  {attribute.oid._name}: {attribute.value}")

    # Serial Number
    print(f"\nSerial Number: {certificate.serial_number}")

    # Validity
    print("\nValidity:")
    print(f"  Not Before: {certificate.not_valid_before_utc}")
    print(f"  Not After:  {certificate.not_valid_after_utc}")

    # Public Key
    public_key = certificate.public_key()
    print("\nPublic Key:")
    print(f"  Algorithm: {public_key.__class__.__name__}")

    # Extensions
    print("\nExtensions:")
    for ext in certificate.extensions:
        print(f"  {ext.oid._name}: {ext.value}")

    # Save the certificate in DER format (optional)
    der_bytes = certificate.public_bytes(Encoding.DER)
    with open("certificate.der", "wb") as der_file:
        der_file.write(der_bytes)

    print(f"\n{'-'*20} end {'-'*20}")

def verify_svid(cert_path: str):
    with open(cert_path, "rb") as cert_file:
        certificate = x509.load_pem_x509_certificate(cert_file.read(), default_backend())
        san = certificate.extensions.get_extension_for_class(x509.SubjectAlternativeName)
        spiffe_id = san.value.get_values_for_type(x509.UniformResourceIdentifier)
        print("SPIFFE ID:", spiffe_id)

        public_key = certificate.public_key()

        try:
            public_key.verify(
                certificate.signature,
                certificate.tbs_certificate_bytes,
                padding.PKCS1v15(),
                certificate.signature_hash_algorithm
            )
            print("certificate verify success")
            now = datetime.utcnow()
            if certificate.not_valid_before <= now <= certificate.not_valid_after:
                print("certificate is valid")
                return True
            else:
                print("certficate is expired or not yet valid")
                return False
        except Exception as e:
            print(f"certificate verify failed: {e}")
            return False

if __name__ == "__main__":
    spiffe_id = "spiffe://fanyamin.com/hello-spiffe"
    cert_path = generate_svid(spiffe_id)
    parse_certificate(cert_path)
    result = verify_svid(cert_path)

    print(f"{spiffe_id} verify result is {result}")