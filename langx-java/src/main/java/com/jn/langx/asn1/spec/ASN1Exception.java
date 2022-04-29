package com.jn.langx.asn1.spec;



import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.NotMutable;
import com.jn.langx.annotation.Nullable;
import com.unboundid.util.LDAPSDKException;



/**
 * This class defines an exception that can be thrown if a problem occurs while
 * interacting with ASN.1 BER elements.
 */
@NotMutable()
public final class ASN1Exception
       extends LDAPSDKException
{
  /**
   * The serial version UID for this serializable class.
   */
  private static final long serialVersionUID = 3234714599495723483L;



  /**
   * Creates a new ASN.1 exception with the provided message.
   *
   * @param  message  A message explaining the problem that occurred.
   */
  public ASN1Exception(@NonNull final String message)
  {
    super(message);
  }



  /**
   * Creates a new ASN.1 exception with the provided message and cause.
   *
   * @param  message  A message explaining the problem that occurred.
   * @param  cause    The underlying cause for this exception.
   */
  public ASN1Exception(@NonNull final String message,
                       @Nullable final Throwable cause)
  {
    super(message, cause);
  }
}
