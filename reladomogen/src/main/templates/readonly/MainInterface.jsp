<%--
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
--%>
<%@ page import="java.util.*,
                 com.gs.fw.common.mithra.generator.*,
                 com.gs.fw.common.mithra.generator.metamodel.MithraObjectType,
                 com.gs.fw.common.mithra.generator.metamodel.AttributeType,
                 com.gs.fw.common.mithra.generator.metamodel.RelationshipType" %>
<%
	MithraObjectTypeWrapper wrapper = (MithraObjectTypeWrapper) request.getAttribute("mithraWrapper");
%>
package <%= wrapper.getPackageName() %>;

<%@  include file="../CvsComment.jspi" %>
public interface <%=wrapper.getInterfaceName()%> extends <%=wrapper.getAbstractInterfaceName()%>
{

}