package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.parser.AspExpr;
import no.uio.ifi.asp.parser.AspSyntax;
import java.util.ArrayList;

public class RuntimeListValue extends RuntimeValue {

    ArrayList<RuntimeValue> list = new ArrayList<RuntimeValue>();

    public RuntimeListValue(ArrayList<RuntimeValue> al) {
        list = al;
    }

    public ArrayList<RuntimeValue> getList(String what, AspSyntax where) {
        return list;
    }

    @Override
    public ArrayList<RuntimeValue> getListValue(String what, AspSyntax where) {
        return list;
    }

    @Override
    String typeName() {return "List";}

    @Override
    public String toString() {
        return list.toString();
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(list.isEmpty());
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return (!list.isEmpty());
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            try {
                int index = (int)v.getIntValue("List subscription", where);
                RuntimeValue elem = list.get(index);

                if (elem instanceof RuntimeIntValue) {
                    return new RuntimeIntValue(elem.getIntValue("List subscription", where));
                }
                if (elem instanceof RuntimeFloatValue) {
                    return new RuntimeFloatValue(elem.getFloatValue("List subscription", where));
                }
                if (elem instanceof RuntimeStringValue) {
                    return new RuntimeStringValue(elem.getStringValue("List subscription", where));
                }
                if (elem instanceof RuntimeBoolValue) {
                    return new RuntimeBoolValue(elem.getBoolValue("List subscription", where));
                }
                if (elem instanceof RuntimeNoneValue) {
                    return new RuntimeNoneValue();
                }
                if (elem instanceof RuntimeListValue) {  
                    return new RuntimeListValue(elem.getListValue("List subscription", where));
                }
                if (elem instanceof RuntimeDictValue) {
                    return new RuntimeDictValue(elem.getDictValue("List subscription", where));
                }
            } catch (IndexOutOfBoundsException e) {
                String err = "List index " + v.toString() + " out of range!";
                runtimeError(err, where);
            }
        }
        else {
            runtimeError("List index must be an integer!", where);
        }
        runtimeError("Subscription '[...]' undefined for "+typeName()+"!", where);
        return null;
        }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            ArrayList<RuntimeValue> resultList = new ArrayList<RuntimeValue>();
            for (int i = 0; i < v.getIntValue("* operand", where); i++) {
                resultList.addAll(list);
            }
            return new RuntimeListValue(resultList);
        }
        runtimeError("Type error for *.", where);
        return null;   
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue((long)list.size());

    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        // Sammenlign to lister
        /*if (v instanceof RuntimeListValue) {
            RuntimeListValue rlv = (RuntimeListValue)v;
            return new RuntimeBoolValue(list.equals(rlv.getList("== operand", where)));
        }*/
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(v.getBoolValue("== operand", where));
        }
        runtimeError("Type error for ==.", where);
        return null;
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        // Sammenlign to lister
        /*if (v instanceof RuntimeListValue) {
            RuntimeListValue rlv = (RuntimeListValue)v;
            return new RuntimeBoolValue(!list.equals(rlv.getList("!= operand", where)));
        }*/
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(!v.getBoolValue("!= operand", where));
        }
        runtimeError("Type error for !=.", where);
        return null;
    }


    @Override
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
        if (inx instanceof RuntimeIntValue) {
            int i = (int)inx.getIntValue("assign", where);
            list.set(i, val);
        }
        
    }


}
