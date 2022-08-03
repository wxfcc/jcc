package xf.vm.jcc.parser;

import java.util.StringJoiner;

/**
 *
 */
public class Modifier {
    public static boolean isPublic(int mod) {
        return (mod & PUBLIC) != 0;
    }

    public static boolean isPrivate(int mod) {
        return (mod & PRIVATE) != 0;
    }

    public static boolean isProtected(int mod) {
        return (mod & PROTECTED) != 0;
    }

    public static boolean isStatic(int mod) {
        return (mod & STATIC) != 0;
    }

    public static boolean isFinal(int mod) {
        return (mod & FINAL) != 0;
    }

    public static boolean isSynchronized(int mod) {
        return (mod & SYNCHRONIZED) != 0;
    }

    public static boolean isVolatile(int mod) {
        return (mod & VOLATILE) != 0;
    }

    public static boolean isTransient(int mod) {
        return (mod & TRANSIENT) != 0;
    }

    public static boolean isInterface(int mod) {
        return (mod & INTERFACE) != 0;
    }

    public static boolean isAbstract(int mod) {
        return (mod & ABSTRACT) != 0;
    }

    public static boolean isStrict(int mod) {
        return (mod & STRICTFP) != 0;
    }

    public static String toString(int mod) {
        StringJoiner sj = new StringJoiner(" ");

        if ((mod & PUBLIC) != 0)        sj.add("public");
        if ((mod & PROTECTED) != 0)     sj.add("protected");
        if ((mod & PRIVATE) != 0)       sj.add("private");

        /* Canonical order */
        if ((mod & ABSTRACT) != 0)      sj.add("abstract");
        if ((mod & STATIC) != 0)        sj.add("static");
        if ((mod & FINAL) != 0)         sj.add("final");
        if ((mod & TRANSIENT) != 0)     sj.add("transient");
        if ((mod & VOLATILE) != 0)      sj.add("volatile");
        if ((mod & SYNCHRONIZED) != 0)  sj.add("synchronized");
        if ((mod & STRICTFP) != 0)        sj.add("strictfp");
        if ((mod & INTERFACE) != 0)     sj.add("interface");

        return sj.toString();
    }

    /*
     * Access modifier flag constants from tables 4.1, 4.4, 4.5, and 4.7 of
     * <cite>The Java Virtual Machine Specification</cite>
     */
    // Standard Java flags.
    public static final int PUBLIC       = 1;		//0x00000001
    public static final int PRIVATE      = 1<<1;	//0x00000002
    public static final int PROTECTED    = 1<<2;	//0x00000004
    public static final int STATIC       = 1<<3;	//0x00000008
    public static final int FINAL        = 1<<4;	//0x00000010
    public static final int SYNCHRONIZED = 1<<5;	//0x00000020
    public static final int VOLATILE     = 1<<6;	//0x00000040
    public static final int TRANSIENT    = 1<<7;	//0x00000080
//    public static final int NATIVE       = 1<<8;	//0x00000100
    public static final int INTERFACE    = 1<<9;	//0x00000200
    public static final int ABSTRACT     = 1<<10;	//0x00000400
    public static final int STRICTFP     = 1<<11;	//0x00000800
    // Flag that marks a symbol synthetic, added in classfile v49.0. 
    public static final int SYNTHETIC    = 1<<12;	//0x00001000
    // Flag that marks attribute interfaces, added in classfile v49.0. 
    public static final int ANNOTATION   = 1<<13;	//0x00002000
    // An enumeration type or an enumeration constant, added in classfile v49.0. 
    public static final int ENUM         = 1<<14;	//0x00004000
    //* Added in SE8, represents constructs implicitly declared in source. 
    public static final int MANDATED     = 1<<15;	//0x00008000

    public static final int StandardFlags = 0x0fff;

    // Bits not (yet) exposed in the public API either because they
    // have different meanings for fields and methods and there is no
    // way to distinguish between the two in this class, or because
    // they are not Java programming language keywords
    static final int BRIDGE    = 0x00000040;
    static final int VARARGS   = 0x00000080;
    
    static boolean isSynthetic(int mod) {
      return (mod & SYNTHETIC) != 0;
    }

    static boolean isMandated(int mod) {
      return (mod & MANDATED) != 0;
    }


    private static final int CLASS_MODIFIERS =
        Modifier.PUBLIC         | Modifier.PROTECTED    | Modifier.PRIVATE |
        Modifier.ABSTRACT       | Modifier.STATIC       | Modifier.FINAL   |
        Modifier.STRICTFP;

    private static final int INTERFACE_MODIFIERS =
        Modifier.PUBLIC         | Modifier.PROTECTED    | Modifier.PRIVATE |
        Modifier.ABSTRACT       | Modifier.STATIC       | Modifier.STRICTFP;


    private static final int CONSTRUCTOR_MODIFIERS =
        Modifier.PUBLIC         | Modifier.PROTECTED    | Modifier.PRIVATE;

    private static final int METHOD_MODIFIERS =
        Modifier.PUBLIC         | Modifier.PROTECTED    | Modifier.PRIVATE |
        Modifier.ABSTRACT       | Modifier.STATIC       | Modifier.FINAL   |
        Modifier.SYNCHRONIZED   | Modifier.STRICTFP;

    private static final int FIELD_MODIFIERS =
        Modifier.PUBLIC         | Modifier.PROTECTED    | Modifier.PRIVATE |
        Modifier.STATIC         | Modifier.FINAL        | Modifier.TRANSIENT |
        Modifier.VOLATILE;

    private static final int PARAMETER_MODIFIERS =
        Modifier.FINAL;

    static final int ACCESS_MODIFIERS =
        Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;

    public static int classModifiers() {
        return CLASS_MODIFIERS;
    }

    public static int interfaceModifiers() {
        return INTERFACE_MODIFIERS;
    }

    public static int constructorModifiers() {
        return CONSTRUCTOR_MODIFIERS;
    }

    public static int methodModifiers() {
        return METHOD_MODIFIERS;
    }

    public static int fieldModifiers() {
        return FIELD_MODIFIERS;
    }

    public static int parameterModifiers() {
        return PARAMETER_MODIFIERS;
    }
    
/*    
    public static String toString(long flags) {
        StringBuilder buf = new StringBuilder();
        String sep = "";
        for (Flag flag : asFlagSet(flags)) {
            buf.append(sep);
            buf.append(flag);
            sep = " ";
        }
        return buf.toString();
    }

    public static EnumSet<Flag> asFlagSet(long flags) {
        EnumSet<Flag> flagSet = EnumSet.noneOf(Flag.class);
        for (Flag flag : Flag.values()) {
            if ((flags & flag.value) != 0) {
                flagSet.add(flag);
                flags &= ~flag.value;
            }
        }
        Assert.check(flags == 0);
        return flagSet;
    }


    // Because the following access flags are overloaded with other
    // bit positions, we translate them when reading and writing class
    // files into unique bits positions: ACC_SYNTHETIC <-> SYNTHETIC,
    // for example.
    public static final int ACC_SUPER    = 0x0020;
    public static final int ACC_BRIDGE   = 0x0040;
    public static final int ACC_VARARGS  = 0x0080;
    public static final int ACC_MODULE   = 0x8000;

    /*****************************************
     * Internal compiler flags (no bits in the lower 16).
     *****************************************

    /** Flag is set if symbol is deprecated.  See also DEPRECATED_REMOVAL.
     
    public static final int DEPRECATED   = 1<<17;

    /** Flag is set for a variable symbol if the variable's definition
     *  has an initializer part.
     
    public static final int HASINIT          = 1<<18;

    /** Flag is set for compiler-generated anonymous method symbols
     *  that `own' an initializer block.
     
    public static final int BLOCK            = 1<<20;

    /** Flag bit 21 is available. (used earlier to tag compiler-generated abstract methods that implement
     *  an interface method (Miranda methods)).
     

    /** Flag is set for nested classes that do not access instance members
     *  or `this' of an outer class and therefore don't need to be passed
     *  a this$n reference.  This value is currently set only for anonymous
     *  classes in superclass constructor calls.
     *  todo: use this value for optimizing away this$n parameters in
     *  other cases.
     *
    public static final int NOOUTERTHIS  = 1<<22;

    /** Flag is set for package symbols if a package has a member or
     *  directory and therefore exists.
     *
    public static final int EXISTS           = 1<<23;

    /** Flag is set for compiler-generated compound classes
     *  representing multiple variable bounds
     *
    public static final int COMPOUND     = 1<<24;

    /** Flag is set for class symbols if a class file was found for this class.
     *
    public static final int CLASS_SEEN   = 1<<25;

    /** Flag is set for class symbols if a source file was found for this
     *  class.
     *
    public static final int SOURCE_SEEN  = 1<<26;

    /* State flags (are reset during compilation).
     *

    /** Flag for class symbols is set and later re-set as a lock in
     *  Enter to detect cycles in the superclass/superinterface
     *  relations.  Similarly for constructor call cycle detection in
     *  Attr.
     *
    public static final int LOCKED           = 1<<27;

    /** Flag for class symbols is set and later re-set to indicate that a class
     *  has been entered but has not yet been attributed.
     *
    public static final int UNATTRIBUTED = 1<<28;

    /** Flag for synthesized default constructors of anonymous classes.
     *
    public static final int ANONCONSTR   = 1<<29; //non-class members

    /**
     * Flag to indicate the superclasses of this ClassSymbol has been attributed.
     *
    public static final int SUPER_OWNER_ATTRIBUTED = 1<<29; //ClassSymbols

    /** Flag for class symbols to indicate it has been checked and found
     *  acyclic.
     *
    public static final int ACYCLIC          = 1<<30;

    /** Flag that marks bridge methods.
     *
    public static final long BRIDGE          = 1L<<31;

    /** Flag that marks formal parameters.
     *
    public static final long PARAMETER   = 1L<<33;

    /** Flag that marks varargs methods.
     *
    public static final long VARARGS   = 1L<<34;

    /** Flag for annotation type symbols to indicate it has been
     *  checked and found acyclic.
     *
    public static final long ACYCLIC_ANN      = 1L<<35;

    /** Flag that marks a generated default constructor.
     *
    public static final long GENERATEDCONSTR   = 1L<<36;

    /** Flag that marks a hypothetical method that need not really be
     *  generated in the binary, but is present in the symbol table to
     *  simplify checking for erasure clashes - also used for 292 poly sig methods.
     *
    public static final long HYPOTHETICAL   = 1L<<37;

    /**
     * Flag that marks an internal proprietary class.
     *
    public static final long PROPRIETARY = 1L<<38;

    /**
     * Flag that marks a multi-catch parameter.
     *
    public static final long UNION = 1L<<39;

    /**
     * Flags an erroneous TypeSymbol as viable for recovery.
     * TypeSymbols only.
     *
    public static final long RECOVERABLE = 1L<<40;

    /**
     * Flag that marks an 'effectively final' local variable.
     *
    public static final long EFFECTIVELY_FINAL = 1L<<41;

    /**
     * Flag that marks non-override equivalent methods with the same signature,
     * or a conflicting match binding (BindingSymbol).
     *
    public static final long CLASH = 1L<<42;

    /**
     * Flag that marks either a default method or an interface containing default methods.
     *
    public static final long DEFAULT = 1L<<43;

    /**
     * Flag that marks class as auxiliary, ie a non-public class following
     * the public class in a source file, that could block implicit compilation.
     *
    public static final long AUXILIARY = 1L<<44;

    /**
     * Flag that marks that a symbol is not available in the current profile
     *
    public static final long NOT_IN_PROFILE = 1L<<45;

    /**
     * Flag that indicates that an override error has been detected by Check.
     *
    public static final long BAD_OVERRIDE = 1L<<45;

    /**
     * Flag that indicates a signature polymorphic method (292).
     *
    public static final long SIGNATURE_POLYMORPHIC = 1L<<46;

    /**
     * Flag that indicates that an inference variable is used in a 'throws' clause.
     *
    public static final long THROWS = 1L<<47;

    /**
     * Flag that marks potentially ambiguous overloads
     *
    public static final long POTENTIALLY_AMBIGUOUS = 1L<<48;

    /**
     * Flag that marks a synthetic method body for a lambda expression
     *
    public static final long LAMBDA_METHOD = 1L<<49;

    /**
     * Flag to control recursion in TransTypes
     *
    public static final long TYPE_TRANSLATED = 1L<<50;

    /**
     * Flag to indicate class symbol is for module-info
     *
    public static final long MODULE = 1L<<51;

    /**
     * Flag to indicate the given ModuleSymbol is an automatic module.
     *
    public static final long AUTOMATIC_MODULE = 1L<<52; //ModuleSymbols only

    /**
     * Flag to indicate the given PackageSymbol contains any non-.java and non-.class resources.
     *
    public static final long HAS_RESOURCE = 1L<<52; //PackageSymbols only

    /**
     * Flag to indicate the given ParamSymbol has a user-friendly name filled.
     *
    public static final long NAME_FILLED = 1L<<52; //ParamSymbols only

    /**
     * Flag to indicate the given ModuleSymbol is a system module.
     *
    public static final long SYSTEM_MODULE = 1L<<53; //ModuleSymbols only

    /**
     * Flag to indicate the given ClassSymbol is a value based.
     *
    public static final long VALUE_BASED = 1L<<53; //ClassSymbols only

    /**
     * Flag to indicate the given symbol has a @Deprecated annotation.
     *
    public static final long DEPRECATED_ANNOTATION = 1L<<54;

    /**
     * Flag to indicate the given symbol has been deprecated and marked for removal.
     *
    public static final long DEPRECATED_REMOVAL = 1L<<55;

    /**
     * Flag to indicate the API element in question is for a preview API.
     *
    public static final long PREVIEW_API = 1L<<56; //any Symbol kind

    /**
     * Flag for synthesized default constructors of anonymous classes that have an enclosing expression.
     *
    public static final long ANONCONSTR_BASED = 1L<<57;

    /**
     * Flag that marks finalize block as body-only, should not be copied into catch clauses.
     * Used to implement try-with-resources.
     *
    public static final long BODY_ONLY_FINALIZE = 1L<<17; //blocks only

    /**
     * Flag to indicate the API element in question is for a preview API.
     *
    public static final long PREVIEW_REFLECTIVE = 1L<<58; //any Symbol kind

    /**
     * Flag to indicate the given variable is a match binding variable.
     *
    public static final long MATCH_BINDING = 1L<<59;

    /**
     * A flag to indicate a match binding variable whose scope extends after the current statement.
     *
    public static final long MATCH_BINDING_TO_OUTER = 1L<<60;

    /**
     * Flag to indicate that a class is a record. The flag is also used to mark fields that are
     * part of the state vector of a record and to mark the canonical constructor
     *
    public static final long RECORD = 1L<<61; // ClassSymbols, MethodSymbols and VarSymbols

    /**
     * Flag to mark a record constructor as a compact one
     *
    public static final long COMPACT_RECORD_CONSTRUCTOR = 1L<<51; // MethodSymbols only

    /**
     * Flag to mark a record field that was not initialized in the compact constructor
     *
    public static final long UNINITIALIZED_FIELD= 1L<<51; // VarSymbols only

    /** Flag is set for compiler-generated record members, it could be applied to
     *  accessors and fields
     *
    public static final int GENERATED_MEMBER = 1<<24; // MethodSymbols and VarSymbols

    /**
     * Flag to indicate sealed class/interface declaration.
     *
    public static final long SEALED = 1L<<62; // ClassSymbols

    /**
     * Flag to indicate that the class/interface was declared with the non-sealed modifier.
     *
    public static final long NON_SEALED = 1L<<63; // ClassSymbols


    /** Modifier masks.
     *
    public static final int
        AccessFlags                       = PUBLIC | PROTECTED | PRIVATE,
        LocalClassFlags                   = FINAL | ABSTRACT | STRICTFP | ENUM | SYNTHETIC,
        StaticLocalFlags                  = LocalClassFlags | STATIC | INTERFACE,
        MemberClassFlags                  = LocalClassFlags | INTERFACE | AccessFlags,
        MemberStaticClassFlags            = MemberClassFlags | STATIC,
        ClassFlags                        = LocalClassFlags | INTERFACE | PUBLIC | ANNOTATION,
        InterfaceVarFlags                 = FINAL | STATIC | PUBLIC,
        VarFlags                          = AccessFlags | FINAL | STATIC |
                                            VOLATILE | TRANSIENT | ENUM,
        ConstructorFlags                  = AccessFlags,
        InterfaceMethodFlags              = ABSTRACT | PUBLIC,
        MethodFlags                       = AccessFlags | ABSTRACT | STATIC | NATIVE |
                                            SYNCHRONIZED | FINAL | STRICTFP,
        RecordMethodFlags                 = AccessFlags | ABSTRACT | STATIC |
                                            SYNCHRONIZED | FINAL | STRICTFP;
    public static final long
        ExtendedStandardFlags             = (long)StandardFlags | DEFAULT | SEALED | NON_SEALED,
        ExtendedMemberClassFlags          = (long)MemberClassFlags | SEALED | NON_SEALED,
        ExtendedMemberStaticClassFlags    = (long) MemberStaticClassFlags | SEALED | NON_SEALED,
        ExtendedClassFlags                = (long)ClassFlags | SEALED | NON_SEALED,
        ModifierFlags                     = ((long)StandardFlags & ~INTERFACE) | DEFAULT | SEALED | NON_SEALED,
        InterfaceMethodMask               = ABSTRACT | PRIVATE | STATIC | PUBLIC | STRICTFP | DEFAULT,
        AnnotationTypeElementMask         = ABSTRACT | PUBLIC,
        LocalVarFlags                     = FINAL | PARAMETER,
        ReceiverParamFlags                = PARAMETER;

    public static Set<Modifier> asModifierSet(long flags) {
        Set<Modifier> modifiers = modifierSets.get(flags);
        if (modifiers == null) {
            modifiers = java.util.EnumSet.noneOf(Modifier.class);
            if (0 != (flags & PUBLIC))    modifiers.add(Modifier.PUBLIC);
            if (0 != (flags & PROTECTED)) modifiers.add(Modifier.PROTECTED);
            if (0 != (flags & PRIVATE))   modifiers.add(Modifier.PRIVATE);
            if (0 != (flags & ABSTRACT))  modifiers.add(Modifier.ABSTRACT);
            if (0 != (flags & STATIC))    modifiers.add(Modifier.STATIC);
            if (0 != (flags & SEALED))    modifiers.add(Modifier.SEALED);
            if (0 != (flags & NON_SEALED))
                                          modifiers.add(Modifier.NON_SEALED);
            if (0 != (flags & FINAL))     modifiers.add(Modifier.FINAL);
            if (0 != (flags & TRANSIENT)) modifiers.add(Modifier.TRANSIENT);
            if (0 != (flags & VOLATILE))  modifiers.add(Modifier.VOLATILE);
            if (0 != (flags & SYNCHRONIZED))
                                          modifiers.add(Modifier.SYNCHRONIZED);
            if (0 != (flags & NATIVE))    modifiers.add(Modifier.NATIVE);
            if (0 != (flags & STRICTFP))  modifiers.add(Modifier.STRICTFP);
            if (0 != (flags & DEFAULT))   modifiers.add(Modifier.DEFAULT);
            modifiers = Collections.unmodifiableSet(modifiers);
            modifierSets.put(flags, modifiers);
        }
        return modifiers;
    }

    // Cache of modifier sets.
    private static final Map<Long, Set<Modifier>> modifierSets = new ConcurrentHashMap<>(64);

    public static boolean isStatic(Symbol symbol) {
        return (symbol.flags() & STATIC) != 0;
    }

    public static boolean isEnum(Symbol symbol) {
        return (symbol.flags() & ENUM) != 0;
    }

    public static boolean isConstant(Symbol.VarSymbol symbol) {
        return symbol.getConstValue() != null;
    }


    public enum Flag {
        PUBLIC(Modifier.PUBLIC),
        PRIVATE(Modifier.PRIVATE),
        PROTECTED(Modifier.PROTECTED),
        STATIC(Modifier.STATIC),
        FINAL(Modifier.FINAL),
        SYNCHRONIZED(Modifier.SYNCHRONIZED),
        VOLATILE(Modifier.VOLATILE),
        TRANSIENT(Modifier.TRANSIENT),
        NATIVE(Modifier.NATIVE),
        INTERFACE(Modifier.INTERFACE),
        ABSTRACT(Modifier.ABSTRACT),
        DEFAULT(Modifier.DEFAULT),
        STRICTFP(Modifier.STRICTFP),
        BRIDGE(Modifier.BRIDGE),
        SYNTHETIC(Modifier.SYNTHETIC),
        ANNOTATION(Modifier.ANNOTATION),
        DEPRECATED(Modifier.DEPRECATED),
        HASINIT(Modifier.HASINIT),
        BLOCK(Modifier.BLOCK),
        ENUM(Modifier.ENUM),
        MANDATED(Modifier.MANDATED),
        NOOUTERTHIS(Modifier.NOOUTERTHIS),
        EXISTS(Modifier.EXISTS),
        COMPOUND(Modifier.COMPOUND),
        CLASS_SEEN(Modifier.CLASS_SEEN),
        SOURCE_SEEN(Modifier.SOURCE_SEEN),
        LOCKED(Modifier.LOCKED),
        UNATTRIBUTED(Modifier.UNATTRIBUTED),
        ANONCONSTR(Modifier.ANONCONSTR),
        ACYCLIC(Modifier.ACYCLIC),
        PARAMETER(Modifier.PARAMETER),
        VARARGS(Modifier.VARARGS),
        ACYCLIC_ANN(Modifier.ACYCLIC_ANN),
        GENERATEDCONSTR(Modifier.GENERATEDCONSTR),
        HYPOTHETICAL(Modifier.HYPOTHETICAL),
        PROPRIETARY(Modifier.PROPRIETARY),
        UNION(Modifier.UNION),
        EFFECTIVELY_FINAL(Modifier.EFFECTIVELY_FINAL),
        CLASH(Modifier.CLASH),
        AUXILIARY(Modifier.AUXILIARY),
        NOT_IN_PROFILE(Modifier.NOT_IN_PROFILE),
        BAD_OVERRIDE(Modifier.BAD_OVERRIDE),
        SIGNATURE_POLYMORPHIC(Modifier.SIGNATURE_POLYMORPHIC),
        THROWS(Modifier.THROWS),
        LAMBDA_METHOD(Modifier.LAMBDA_METHOD),
        TYPE_TRANSLATED(Modifier.TYPE_TRANSLATED),
        MODULE(Modifier.MODULE),
        AUTOMATIC_MODULE(Modifier.AUTOMATIC_MODULE),
        SYSTEM_MODULE(Modifier.SYSTEM_MODULE),
        DEPRECATED_ANNOTATION(Modifier.DEPRECATED_ANNOTATION),
        DEPRECATED_REMOVAL(Modifier.DEPRECATED_REMOVAL),
        HAS_RESOURCE(Modifier.HAS_RESOURCE),
        POTENTIALLY_AMBIGUOUS(Modifier.POTENTIALLY_AMBIGUOUS),
        ANONCONSTR_BASED(Modifier.ANONCONSTR_BASED),
        NAME_FILLED(Modifier.NAME_FILLED),
        PREVIEW_API(Modifier.PREVIEW_API),
        PREVIEW_REFLECTIVE(Modifier.PREVIEW_REFLECTIVE),
        MATCH_BINDING(Modifier.MATCH_BINDING),
        MATCH_BINDING_TO_OUTER(Modifier.MATCH_BINDING_TO_OUTER),
        RECORD(Modifier.RECORD),
        RECOVERABLE(Modifier.RECOVERABLE),
        SEALED(Modifier.SEALED),
        NON_SEALED(Modifier.NON_SEALED) {
            @Override
            public String toString() {
                return "non-sealed";
            }
        };

        Flag(long flag) {
            this.value = flag;
            this.lowercaseName = StringUtils.toLowerCase(name());
        }

        @Override
        public String toString() {
            return lowercaseName;
        }

        final long value;
        final String lowercaseName;
    }
*/
}
