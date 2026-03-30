/**
 * This file is part of SDUtils, which is a library of useful classes and functionality.
 * Copyright (c) 2023, SerpentDagger (MRRH) <serpentdagger.contact@gmail.com>.
 * 
 * SDUtils is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * 
 * SDUtils is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with SDUtils.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package group;

public class MixedPair<A, B>
{
	protected A a;
	protected B b;
	
	public MixedPair(A a, B b)
	{
		this.a = a;
		this.b = b;
	}

	public A a()
	{
		return a;
	}

	public MixedPair<A, B> setA(A a)
	{
		this.a = a;
		return this;
	}

	public B b()
	{
		return b;
	}

	public MixedPair<A, B> setB(B b)
	{
		this.b = b;
		return this;
	}
}
