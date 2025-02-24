package no.uio.ifi.asp.runtime;

import java.util.Objects;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.parser.AspSyntax;


public class RuntimeStringValue extends RuntimeValue {
    
    String stringvalue;

    public RuntimeStringValue(String s) {
        stringvalue = s;
    }

    @Override
    String typeName() {
        return "String";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RuntimeStringValue) {
            RuntimeStringValue rv = (RuntimeStringValue)o;
            return Objects.equals(stringvalue, rv.stringvalue);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stringvalue);
    }

    public String showInfo() {
        if (stringvalue.indexOf('\'') >= 0) {
            return '"' + stringvalue + '"';
        } else {
            return "'" + stringvalue + "'";
        }
    }

    @Override
    public String toString() {
        return "'" + stringvalue + "'";
    }

    @Override
    public String getStringValue(String what, AspSyntax where) {
        return stringvalue;
    }

    @Override
    public long getIntValue(String what, AspSyntax where) {
        return Long.parseLong(stringvalue);   
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return (stringvalue != "");
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        double d = Double.parseDouble(stringvalue);
        return d;
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!this.getBoolValue(typeName(), where));
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue){
            return new RuntimeStringValue(stringvalue.concat(v.getStringValue("+ operand", where)));
        }
        runtimeError("Type error for +.", where);
        return  null;
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(stringvalue.length()); 
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if(v instanceof  RuntimeStringValue) {
            return new RuntimeBoolValue(stringvalue.equals(v.getStringValue("== operand", where)));
        }
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(v.getBoolValue("!= operand", where));
        }
        runtimeError("Type error for ==.", where);
        return null;
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeBoolValue(!stringvalue.equals(v.getStringValue("!= operand", where)));
        }
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(!v.getBoolValue("!= operand", where));
        }
        runtimeError("Type error for !=.", where);
        return null;
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            int index = (int)v.getIntValue(v.typeName(), where);
            return new RuntimeStringValue((String.valueOf(stringvalue.charAt(index))));
        }
        runtimeError("Type error for subscription.", where);
        return null;
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            int result = stringvalue.compareTo(v.getStringValue("> operand", where));
            if (result > 0) {
                return new RuntimeBoolValue(true);
            } 
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for >.", where);
        return null;
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            int result = stringvalue.compareTo(v.getStringValue(">= operand", where));
            if (result < 0) {
                return new RuntimeBoolValue(false);
            }
            return new RuntimeBoolValue(true);
        }
        runtimeError("Type error for >=.", where);
        return null;
    }
    
    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            int result = stringvalue.compareTo(v.getStringValue("< operand", where));
            if (result < 0) {
                return new RuntimeBoolValue(true);
            }
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for <.", where);
        return null;
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            int result = stringvalue.compareTo(v.getStringValue("<= operand", where));
            if (result > 0) {
                return new RuntimeBoolValue(false);
            }
            return new RuntimeBoolValue(true);
        }
        runtimeError("Type error for <=.", where);
        return null;
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeIntValue){
           long number = v.getIntValue("* operand", where);
           String s = stringvalue;
           StringBuilder result = new StringBuilder();
            for (int i = 0; i < number; i++) {
                result.append(s);
            }
            return new RuntimeStringValue(result.toString());
        }
        runtimeError("Type error for *.", where);
        return null;
    }
}
