package utilities;

public class EnumUtils
{
	public static <E extends Enum<E>> E valueOf(Class<E> cl, String str, E defalt)
	{
		for (E e : cl.getEnumConstants())
			if (e.name().equals(str))
				return e;
		return defalt;
	}
}
