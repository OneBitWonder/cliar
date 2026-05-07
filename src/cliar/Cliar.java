/*
 * Copyright (C) 2026 onebitwonder
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cliar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a lightweight command-line argument parser for Java applications.
 * <p>
 * {@code Cliar} supports parsing of UNIX-style short options
 * (e.g., {@code -v} or grouped {@code -abc}), GNU-style long options
 * (e.g., {@code --verbose} or {@code --output=file.txt}), and positional
 * arguments.
 * <p>
 * Supported options must be declared explicitly using {@link Option}
 * definitions before parsing. Options may be marked as required and may
 * optionally expect an associated value.
 * <p>
 * Short options are treated as boolean flags and may be grouped together.
 * Long options may optionally carry values using the {@code --key=value}
 * syntax.
 * <p>
 * Arguments that do not begin with {@code -} or {@code --} are interpreted
 * as positional arguments and preserved in the order they were supplied.
 * <p>
 * The class is designed as a small, dependency-free utility rather than a
 * full-featured command-line parsing framework.
 *
 * @author onebitwonder
 */
public class Cliar {
    
    /**
     * Describes a command-line option that can be recognized by {@link Cliar}.
     * <p>
     * An option may have a short name (e.g., {@code -v}), a long name
     * (e.g., {@code --verbose}), or both. At least one of these must be provided.
     * Short options are restricted to a single character, while long options may
     * consist of multiple characters.
     * <p>
     * Options can be marked as required, in which case they must be present in the
     * parsed arguments. Additionally, options may declare whether they expect an
     * associated value (e.g., {@code --output=file.txt}).
     * <p>
     * This class is a simple data holder and does not perform parsing itself.
     */
    public static class Option {
        
        /**
         * Constructs a new {@code Option} with the specified properties.
         *
         * @param shortOption the single-character short option name (without leading {@code -}),
         *                    or {@code null} if not defined
         * @param longOption  the long option name (without leading {@code --}),
         *                    or {@code null} if not defined
         * @param description a human-readable description of the option
         * @param required    whether this option must be present in the input
         * @param expectsValue whether this option requires an associated value
         * @throws IllegalArgumentException if both {@code shortOption} and {@code longOption}
         *                                  are {@code null} or blank, or if the short option
         *                                  is not a single character
         */
        public Option(String shortOption, String longOption, String description, boolean required, boolean expectsValue) {
            
            if ((shortOption == null || shortOption.isBlank()) && (longOption == null || longOption.isBlank())) {
                throw new IllegalArgumentException("Option requires short or long name.");
            }

            if ((shortOption != null) && (1 != shortOption.length())) {
                throw new IllegalArgumentException("Short option must be a single character.");
            }
            
            if (shortOption != null && expectsValue) {
                throw new IllegalArgumentException("Short options cannot expect values");
            }
            
            this.shortOption = shortOption;
            this.longOption = longOption;
            this.description = description;
            this.required = required;
            this.expectsValue = expectsValue;
        }
        
        private final String shortOption;
        
        /**
         * Returns the short option name.
         *
         * @return the short option (without leading {@code -}), or {@code null} if not defined
         */
        public String getShortOption() {
            return shortOption;
        }
        
        /**
         * Indicates whether this option defines a short name.
         *
         * @return {@code true} if a non-blank short option is present, {@code false} otherwise
         */
        public boolean hasShortOption() {
            return (null != shortOption) && !shortOption.isBlank();
        }
        
        private final String longOption;
        
        /**
         * Returns the long option name.
         *
         * @return the long option (without leading {@code --}), or {@code null} if not defined
         */
        public String getLongOption() {
            return longOption;
        }
        
        /**
         * Indicates whether this option defines a long name.
         *
         * @return {@code true} if a non-blank long option is present, {@code false} otherwise
         */
        public boolean hasLongOption() {
            return (null != longOption) && !longOption.isBlank();
        }
        
        private final String description;
        
        /**
         * Returns the description of this option.
         *
         * @return a human-readable description
         */
        public String getDescription() {
            return description;
        }
        
        private final boolean required;
        
        /**
         * Indicates whether this option is required.
         *
         * @return {@code true} if the option must be present, {@code false} otherwise
         */
        public boolean isRequired() {
            return required;
        }
        
        private final boolean expectsValue;
        
        /**
         * Indicates whether this option expects a value.
         *
         * @return {@code true} if the option requires a value, {@code false} otherwise
         */
        public boolean expectsValue() {
            return expectsValue;
        }
    }
    
    /**
     * Private constructor to prevent direct instantiation. Instances are created
     * exclusively through the {@link #from(String[])} factory method.
     */
    private Cliar() {
        ;
    }
    
    public String help() {
        return "NOT IMPLEMENTED YET.";
    }
    
    /**
     * Maps short option names (without leading {@code -}) to their corresponding {@link Option} definitions.
     */
    private final Map<String, Option> shortOptions = new HashMap<>();

    /**
     * Maps long option names (without leading {@code --}) to their corresponding {@link Option} definitions.
     */
    private final Map<String, Option> longOptions = new HashMap<>();

    /**
     * Stores positional arguments in the order they were provided on the command line.
     */
    private final List<String> positionalArguments = new ArrayList<>();
    
    /**
     * Stores parsed options and their associated values.
     * <p>
     * For options that do not expect a value, the mapped value is {@code null}.
     */
    private final Map<Option, String> parsedOptions = new HashMap<>();
    
    /**
     * Parses a sequence of short options and updates the provided {@link Cliar} instance.
     * <p>
     * The argument is expected to contain one or more single-character options
     * (e.g., {@code a} or {@code abc}), corresponding to the command-line forms
     * {@code -a} or {@code -abc}. Each character is treated as an individual option.
     * <p>
     * All options must be alphabetic and must be defined in the set of registered
     * short options. Each recognized option is stored as a flag with an implicit
     * value of {@code null}.
     *
     * @param arg   the short option string without the leading {@code -}
     * @param cliar the {@code Cliar} instance to update
     * @throws IllegalArgumentException if the argument is blank, contains invalid
     *                                  characters, or references an undefined option
     */
    private static void parseShortOption(String arg, Cliar cliar) throws IllegalArgumentException  {
        if (arg.isBlank()) {
            throw new IllegalArgumentException("Argument is empty.");
        }
        
        for (char chr : arg.toCharArray()) {
            if (Character.isLetter(chr)) {
                Option opt = cliar.shortOptions.get(String.valueOf(chr));
                
                if (null != opt) {
                    
                    if (cliar.parsedOptions.containsKey(opt)) {
                        throw new IllegalArgumentException("Duplicate short option: -" + opt.shortOption);
                    }

                    cliar.parsedOptions.put(opt, null);
                    continue;
                }
                
                throw new IllegalArgumentException(String.format("Invalid option '%c' in argument %s.", chr, arg));
            } else {
                throw new IllegalArgumentException(String.format("Invalid option '%c' in argument %s.", chr, arg));
            }
        }
    }
    
    /**
     * Parses a long option and updates the provided {@link Cliar} instance.
     * <p>
     * The argument is expected to represent a long option without the leading
     * {@code --}, either in the form {@code key} or {@code key=value}. If a value
     * is provided using {@code =}, everything before the separator is treated as
     * the option name and everything after as its associated value.
     * <p>
     * The option name must match a previously registered long option. If the option
     * expects a value, it must be provided using the {@code key=value} syntax.
     * Otherwise, the option is treated as a boolean flag with an implicit value of
     * {@code null}.
     *
     * @param arg   the long option string without the leading {@code --}
     * @param cliar the {@code Cliar} instance to update
     * @throws IllegalArgumentException if the argument is blank, malformed,
     *                                  references an undefined option, or violates
     *                                  the option's value requirements
     */
    private static void parseLongOption(String arg, Cliar cliar) throws IllegalArgumentException {
        if (arg.isBlank()) {
            throw new IllegalArgumentException("Argument is empty.");            
        }
        
        int index = arg.indexOf('=');
        String key = arg;
        String val = null;
        
        if (-1 != index) {
            key = arg.substring(0, index);
            val = arg.substring(index + 1);
        }

        if (key.isEmpty()) {// TODO: allows blanks but not zero lenght value. why not zero lenght? maybe allow that too?
            throw new IllegalArgumentException("Argument is empty.");            
        }

        // TODO: maybe remove this? will allow nearly any character; restrict to non-control characters?
        // Must start with a letter
        if (! Character.isLetter(key.charAt(0))) {
            throw new IllegalArgumentException("Argument name must start with a letter.");            
        }

        // May only contain letter, digit, hyphen
        for (int i = 1; i < key.length(); i++) {
            char chr = key.charAt(i);

            if (!(Character.isLetter(chr) || Character.isDigit(chr) || ('-' == chr))) {
                throw new IllegalArgumentException(String.format("Illegal character %c in argument %s at position %d.", chr, key,i));
            }
        }
        
        Option opt = cliar.longOptions.get(key);
        
        if (null == opt) {
            throw new IllegalArgumentException(String.format("Invalid option '%s'.", key));
        }
        
        if (opt.expectsValue && ((index == -1) || val.isBlank())) {
            throw new IllegalArgumentException(String.format("Option '%s' required a value.", key));
        }
        
        if (cliar.parsedOptions.containsKey(opt)) {
            throw new IllegalArgumentException("Duplicate long option: --" + opt.longOption);
        }

        cliar.parsedOptions.put(opt, val);
    }
    
    /**
     * Parses the supplied command-line arguments and constructs a {@code Cliar}
     * instance containing the recognized options and positional arguments.
     * <p>
     * Before parsing, all supported options must be declared via the provided
     * {@code Option[]} array. Both short and long options are supported:
     * <ul>
     *   <li>Short options use the {@code -x} syntax and may be grouped
     *       (e.g., {@code -abc}). Each character represents a separate flag.</li>
     *   <li>Long options use the {@code --key} or {@code --key=value} syntax.</li>
     * </ul>
     * <p>
     * Options that do not expect a value are treated as boolean flags. Options that
     * expect a value must use the {@code --key=value} form.
     * <p>
     * Arguments that do not start with {@code -} or {@code --} are treated as
     * positional arguments and are preserved in the order they were provided.
     * <p>
     * After parsing, all required options are validated. If a required option is
     * missing, an exception is thrown.
     *
     * @param args     the command-line arguments to parse
     * @param options  the set of supported options
     * @return a {@code Cliar} instance containing the parsed result
     * @throws IllegalArgumentException if an argument is malformed, references an
     *                                  unknown option, violates option constraints,
     *                                  or if a required option is missing
     */
    public static Cliar from(String[] args, Option[] options) throws IllegalArgumentException {
        Cliar cliar = new Cliar();
        
        for (Option opt : options) {
            if (options == null) {
                throw new IllegalArgumentException("Options must not be null.");
            }
            
            if (opt.hasShortOption()) {
                if (cliar.shortOptions.containsKey(opt.shortOption)) {
                    throw new IllegalArgumentException("Duplicate short option: -" + opt.shortOption);
                }
                
                cliar.shortOptions.put(opt.shortOption, opt);
            }
            
            if (opt.hasLongOption()) {
                if (cliar.longOptions.containsKey(opt.longOption)) {
                    throw new IllegalArgumentException("Duplicate long option: --" + opt.longOption);
                }
                
                cliar.longOptions.put(opt.longOption, opt);
            }
        }
        
        if (args == null) {
            throw new IllegalArgumentException("Arguments must not be null.");
        }
        
        for (String arg : args) {
            if (arg.startsWith("--") && (2 < arg.length())) {
                parseLongOption(arg.substring(2), cliar);
            } else if (arg.startsWith("-") && !arg.startsWith("--") && (1 < arg.length())) { // NOTE: 1 < arg.length turns special option '-' into a positional argument
                parseShortOption(arg.substring(1), cliar);
            } else {
                cliar.positionalArguments.add(arg);
            }
        }
        
        for (Option opt : options) {
            if (opt.isRequired() && !cliar.parsedOptions.containsKey(opt)) {
                // NOTE: preference is longOption > shortOption
                String name = opt.hasLongOption() ? "--" + opt.getLongOption() : "-" + opt.getShortOption();
                throw new IllegalArgumentException(String.format("Missing required option %s.", name));
            }
        }
        
        return cliar;
    }
}