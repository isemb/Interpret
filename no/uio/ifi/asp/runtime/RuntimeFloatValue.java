package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;


public class RuntimeFloatValue extends  RuntimeValue {
    
    double floatvalue;

    public  RuntimeFloatValue(double f) {
        floatvalue = f;
    }

    @Override
    String typeName() {
        return "float";
    }

    @Override
    public String toString() {
        return String.valueOf(floatvalue);
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return (floatvalue != 0.0);
    }
    
    @Override
    public double getFloatValue(String what, AspSyntax where) {
        return (double)floatvalue;
    }

    @Override
    public long getIntValue(String what, AspSyntax where) {
        return (int)floatvalue;
    }

    @Override
    public String getStringValue(String what, AspSyntax where) {
        return String.valueOf(floatvalue);
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue ) {
            return new RuntimeFloatValue(floatvalue + v.getFloatValue("+ operand", where));
        }
        runtimeError("Type error for +.", where);
        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue) {
            double divisor = v.getFloatValue("/ operand", where);
            double result = floatvalue / divisor;
            return new RuntimeFloatValue(result);
        }
        runtimeError("'/' undefined for "+typeName()+"!", where);
        return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(floatvalue == v.getFloatValue("== operand", where));
        }
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(v.getBoolValue("== operand", where));
        }
        runtimeError("Type error for ==.", where);
        return null;
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(floatvalue != v.getFloatValue("!= operand", where));
        }
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(!v.getBoolValue("!= operand", where));
        }
        runtimeError("Type error for !=.", where);
        return null;
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(floatvalue > v.getFloatValue("> operand", where));
        }
        runtimeError("Type error for >.", where);
        return null;
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(floatvalue >= v.getFloatValue(">= operand", where));
        }
        runtimeError("'>=' undefined for "+typeName()+"!", where);
        return null;
    }

    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue) {
            double result = floatvalue / v.getFloatValue("/ operand", where);
            return new RuntimeFloatValue((long)Math.floor(result));
        }
        runtimeError("Type error for //.", where);
        return null;
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(floatvalue < v.getFloatValue("< operand", where));
        }
        runtimeError("Type error for <.", where);
        return null;
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(floatvalue <= v.getFloatValue("<= operand", where));
        }
        runtimeError("Type error for <=.", where);
        return null;
    }

    @Override
    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue) {
            long v_result = v.getIntValue(v.typeName(), where);
            double mod_res = Math.floorMod(this.getIntValue("int", where), v_result );
            return new RuntimeFloatValue(mod_res);
        }
        runtimeError("Type error for %.", where);
        return null;
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatvalue * v.getFloatValue("* operand", where));
        }
        runtimeError("Type error for *.", where);
        return null;
    }
    
    @Override
    public RuntimeValue evalNegate(AspSyntax where) {
        return new RuntimeFloatValue(-floatvalue);
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!this.getBoolValue(typeName(), where));
    }

    @Override
    public RuntimeValue evalPositive(AspSyntax where) {
        return new RuntimeFloatValue(+floatvalue);
    }

    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatvalue - v.getFloatValue("- operand", where));
        }
        runtimeError("Type error for -.", where);
        return null;
    }
}
