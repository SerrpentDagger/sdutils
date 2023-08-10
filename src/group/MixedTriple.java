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

public class MixedTriple<A, B, C> extends MixedPair<A, B>
{
	protected C c;

	public MixedTriple(A a, B b, C c)
	{
		super(a, b);
		this.c = c;
	}

	@Override
	public MixedTriple<A, B, C> setA(A a)
	{
		super.setA(a);
		return this;
	}
	
	@Override
	public MixedTriple<A, B, C> setB(B b)
	{
		super.setB(b);
		return this;
	}
	
	public C c()
	{
		return c;
	}

	public MixedTriple<A, B, C> setC(C c)
	{
		this.c = c;
		return this;
	}
}
