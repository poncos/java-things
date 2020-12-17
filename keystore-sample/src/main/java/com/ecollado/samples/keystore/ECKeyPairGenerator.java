package com.ecollado.samples.keystore;


import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.X500Name;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by poncos on 6/14/15.
 */
public class ECKeyPairGenerator
{
    private static final String CERT_ALGORITHM = "RSA";

    private static final String SIGN_ALGORITHM = "SHA256WithRSA";

    private static final String SK_ALGORITHM = "AES";

    private static final String KEYSTORE_TYPE = "JKS";

    private static final String KEYSTORE_PASSWORD = "password";

    private static final String CERT_PASSWORD = "password";

    private static final String ALIAS = "trayning";

    private static final String DN = "CN=trayning,O=ecollado,L=cork,C=ie";


    public SecretKey getSecretKey()
    {
        try
        {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(SK_ALGORITHM);
            return keyGenerator.generateKey();

        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public KeyPair getPrivateKey()
    {
        try
        {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(CERT_ALGORITHM);
            return keyPairGenerator.generateKeyPair();

        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public X509Certificate genCertificate()
    {
        try
        {
            CertAndKeyGen certGen = new CertAndKeyGen(CERT_ALGORITHM, SIGN_ALGORITHM, null);
            certGen.generate(2048);

            // valid for a year
            long validSecs = (long) 365 * 24 * 60 * 60;
            X509Certificate cert = certGen.getSelfCertificate(
                    // enter your details according to your application
                    new X500Name(DN), validSecs);

            return cert;
        }
        catch (InvalidKeyException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchProviderException e)
        {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public KeyStore load(String keyStoreFileName)
    {
        KeyStore keyStore = null;
        File keyStoreFile = new File(keyStoreFileName);
        FileInputStream fis = null;

        try
        {
            keyStore = KeyStore.getInstance(KEYSTORE_TYPE);

            if (keyStoreFile.exists())
            {
                try
                {
                    fis = new FileInputStream(keyStoreFileName);
                    keyStore.load(fis, KEYSTORE_PASSWORD.toCharArray());

                    KeyStore.PasswordProtection passwordProtection =
                            new KeyStore.PasswordProtection(KEYSTORE_PASSWORD.toCharArray());

                    KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry)
                            keyStore.getEntry(ALIAS, passwordProtection);

                    if (pkEntry != null)
                    {
                        java.security.cert.Certificate[] chain = pkEntry.getCertificateChain();
                        if (chain != null)
                        {
                            System.out.println("Chain: " + chain[0].getType());
                        }

                        PrivateKey privateKey = pkEntry.getPrivateKey();
                        if (privateKey != null)
                        {
                            System.out.println("PrivateKey: " + privateKey.getAlgorithm());
                        }

                    }

                }
                catch (UnrecoverableEntryException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    if (fis != null)
                    {
                        fis.close();
                    }
                }
            }
            else
            {
                keyStore.load(null);

                //KeyStore.SecretKeyEntry skEntry =
                //        new KeyStore.SecretKeyEntry(this.getSecretKey());

                //KeyPair keyPair = this.getPrivateKey();

                //X509Certificate x509Certificate = this.genCertificate();

                //KeyStore.PrivateKeyEntry pkEntry =
                //        new KeyStore.PrivateKeyEntry(keyPair.getPrivate(),
                //                new X509Certificate[] {x509Certificate});

                //KeyStore.TrustedCertificateEntry trustedCertificateEntry =
                //        new KeyStore.TrustedCertificateEntry(x509Certificate);

                //keyStore.setEntry(ALIAS,skEntry,null);
                //keyStore.setEntry(ALIAS, pkEntry,null);
                //keyStore.setEntry(ALIAS, trustedCertificateEntry, null);

                //keyStore.setKeyEntry(ALIAS,keyPair.getPrivate(),null,new X509Certificate[] {x509Certificate});

                CertAndKeyGen certGen = new CertAndKeyGen("RSA", "SHA256WithRSA", null);
                certGen.generate(2048);

                // valid for a year
                long validSecs = (long) 365 * 24 * 60 * 60;
                X509Certificate cert = certGen.getSelfCertificate(
                        // enter your details according to your application
                        new X500Name(DN), validSecs);

                keyStore.setKeyEntry(ALIAS, certGen.getPrivateKey(), KEYSTORE_PASSWORD.toCharArray(), new X509Certificate[]{cert});
            }
            return keyStore;
        }
        catch (KeyStoreException e)
        {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }

        return keyStore;
    }

    public KeyStore createTrustStore(KeyStore keyStore)
    {
        KeyStore.PasswordProtection passwordProtection =
                new KeyStore.PasswordProtection(KEYSTORE_PASSWORD.toCharArray());

        KeyStore.PrivateKeyEntry pkEntry = null;
        try
        {
            pkEntry = (KeyStore.PrivateKeyEntry)
                    keyStore.getEntry(ALIAS, passwordProtection);

            if (pkEntry != null)
            {
                java.security.cert.Certificate[] chain = pkEntry.getCertificateChain();
                if (chain != null)
                {
                    System.out.println("Chain: " + chain[0].getType());
                }

                KeyStore trustStore = KeyStore.getInstance(KEYSTORE_TYPE);
                trustStore.load(null);

                KeyStore.TrustedCertificateEntry entry =
                        new KeyStore.TrustedCertificateEntry(chain[0]);

                trustStore.setEntry(ALIAS, entry, null);

                return trustStore;
            }
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnrecoverableEntryException e)
        {
            e.printStackTrace();
        }
        catch (KeyStoreException e)
        {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void save(KeyStore keyStore, String file)
    {
        // store away the keystore
        java.io.FileOutputStream fos = null;
        try
        {
            fos = new java.io.FileOutputStream(file);
            keyStore.store(fos, KEYSTORE_PASSWORD.toCharArray());
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (CertificateException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (KeyStoreException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e) {}
            }
        }
    }
}
