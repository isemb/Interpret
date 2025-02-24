package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.parser.AspSyntax;
import no.uio.ifi.asp.scanner.TokenKind;

public class RuntimeDictValue extends RuntimeValue {

    HashMap<RuntimeStringValue, RuntimeValue> dict = new HashMap<RuntimeStringValue, RuntimeValue>();

    public RuntimeDictValue(HashMap<RuntimeStringValue, RuntimeValue> d) {
        dict = d;
    }

    public HashMap<RuntimeStringValue, RuntimeValue> getDict(String what, AspSyntax where) {
        return dict;
    }

    public HashMap<RuntimeStringValue, RuntimeValue> getDictValue(String what, AspSyntax where) {return dict; }

    @Override
    String typeName() {
        return "Dict";
    }

    @Override
    public String toString() {
        if (dict.isEmpty()) {
            return "{}";
        }
        StringBuilder formattedMap = new StringBuilder("{");
        for (Map.Entry<RuntimeStringValue, RuntimeValue> entry : dict.entrySet()) {
            formattedMap.append(entry.getKey().showInfo()).append(": ").append(entry.getValue().showInfo()).append(", ");
        }
        formattedMap.setLength(formattedMap.length() - 2);
        formattedMap.append("}");

        return (formattedMap.toString());
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(dict.isEmpty());
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return (!dict.isEmpty());
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where){
        return  new RuntimeIntValue((long)dict.size());

    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        // Sammenlign to dictionaries
        /*if (v instanceof RuntimeDictValue) {
            RuntimeDictValue rdv = (RuntimeDictValue)v;
            if (this.dict.isEmpty() && rdv.dict.isEmpty()) {
                return new RuntimeBoolValue(true);
            }
            if (this.dict.size() != rdv.dict.size()) {
                return new RuntimeBoolValue(false);
            }
            for (Map.Entry<RuntimeStringValue, RuntimeValue> entry : this.dict.entrySet()) {
                RuntimeStringValue key = entry.getKey();
                RuntimeValue value = entry.getValue();
    
                if (rdv.dict.containsKey(key)) {
                    
                    if (value.evalEqual(rdv.dict.get(key), where).getBoolValue("Dict", where)) {
                        continue;
                    }
                }
                return new RuntimeBoolValue(false);
            }
            return new RuntimeBoolValue(true);
        }*/
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(v.getBoolValue("== operand", where));
        }
        runtimeError("Type error for ==.", where);
        return null;
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        // Sammenlign to dictionaries
        /*if (v instanceof RuntimeDictValue) {
            RuntimeBoolValue rv = (RuntimeBoolValue)evalEqual(v, where);
            return new RuntimeBoolValue(!rv.getBoolValue("Dict", where));
        }*/
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(!v.getBoolValue("!= operand", where));
        }
        runtimeError("Type error for !=.", where);
        return null;
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        for (RuntimeStringValue key : dict.keySet()) {
            if (key.evalEqual(v, where).getBoolValue("Dict subscription", where)) {
                RuntimeValue value = dict.get(key);
                if (value instanceof RuntimeIntValue) {
                    return new RuntimeIntValue(value.getIntValue("Dict subscription", where));
                }
                if (value instanceof RuntimeFloatValue) {
                    return new RuntimeFloatValue(value.getFloatValue("Dict subscription", where));
                }
                if (value instanceof RuntimeStringValue) {
                    return new RuntimeStringValue(value.getStringValue("Dict subscription", where));
                }
                if (value instanceof RuntimeBoolValue) {
                    return new RuntimeBoolValue(value.getBoolValue("Dict subscription", where));
                }
                if (value instanceof RuntimeNoneValue) {
                    return new RuntimeNoneValue();
                }
            }
        }
        String err = "Dictionary key " + v.toString() + " undefined!";
        runtimeError(err, where);
        return null;
    }

    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
        boolean exists = false;
        if (inx instanceof RuntimeStringValue) {
            for (RuntimeStringValue key: dict.keySet()) {
                if (key.evalEqual(inx, where).getBoolValue("x", where)) {
                    dict.put(key, val);
                    exists = true;
                }
            }
            if (!exists)  {
                dict.put((RuntimeStringValue)inx, val);
            }
        } else {
            runtimeError("Type error: index is not a text string!", where);
        }
    }
}
