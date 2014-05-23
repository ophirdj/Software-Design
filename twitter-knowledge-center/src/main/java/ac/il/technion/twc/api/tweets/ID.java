package ac.il.technion.twc.api.tweets;

/**
 * 
 * @author Ophir De Jager
 * 
 */
public class ID {

  /**
   * Identification string.
   */
  public final String id;

  /**
   * @param id
   *          An alpha-numeric string.
   */
  public ID(final String id) {
    this.id = id;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final ID other = (ID) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  /**
   * @param string
   * @return true if the string is a legal id.
   */
  public static boolean isID(final String string) {
    return string.matches("[A-Za-z0-9]+");
  }

}
