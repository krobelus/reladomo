/*
 Copyright 2016 Goldman Sachs.
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

/* Generated By:JJTree: Do not edit this line. ASTInOperator.java */

package com.gs.fw.common.mithra.generator.queryparser;

public class ASTInOperator extends SimpleNode implements Operator
{
	public ASTInOperator(int id)
	{
		super(id);
	}

	public ASTInOperator(MithraQL p, int id)
	{
		super(p, id);
	}


	/**
	 * Accept the visitor. *
	 */
	public Object jjtAccept(MithraQLVisitor visitor, Object data)
	{
		return visitor.visit(this, data);
	}

	public boolean isIsNull()
	{
		return false;
	}

	public boolean isGreaterThan()
	{
		return false;
	}

	public boolean isLesserThan()
	{
		return false;
	}

	public boolean isGreaterThanOrEqualTo()
	{
		return false;
	}

	public boolean isLesserThanOrEqualTo()
	{
		return false;
	}

	public boolean isEqual()
	{
		return false;
	}

	public boolean isNotEqual()
	{
		return false;
	}

	public Operator getReverseOperator()
	{
		return this;
	}

	public boolean isIn()
	{
		return true;
	}

	public String getMethodName()
	{
		return "in";
	}

	public String toString()
	{
		return "in";
	}

    public boolean equalsOther(SimpleNode other)
    {
        return other instanceof ASTInOperator;
    }

    public boolean isUnary()
    {
        return false;
    }

    public boolean isEqualsEdgePoint()
    {
        return false;
    }

    public String getPrimitiveExpression(String left, String right)
    {
        return right+".contains("+left+")";
    }

    public String getNonPrimitiveExpression(String left, String right)
    {
        return right+".contains("+left+")";
    }
}
