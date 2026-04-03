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
 * Represents a collection of boolean command-line flags for an application.
 * <p>
 * This class provides simple parsing and storage of UNIX-style short options
 * (e.g., {@code -x} or grouped {@code -xyz}) as well as GNU-style long options
 * (e.g., {@code --verbose} or {@code --output=file.txt}). Short options are
 * single-character alphabetic flags. Long options may optionally include a
 * value using the {@code --key=value} syntax.
 * <p>
 * The class is designed as a lightweight, drop-in utility rather than a
 * full-featured command-line parsing framework.
 * 
 * @author onebitwonder
 */
public class Cliar {
    
    /**
     * Private constructor to prevent direct instantiation. Instances are created
     * exclusively through the {@link #from(String[])} factory method.
    */
    private Cliar() {
        ;
    }
    
    /**
     * Stores the collection of flags supplied to the application.
     * <p>
     * Each flag is treated as a boolean setting: a flag is considered {@code true}
     * when present on the command line and {@code false} when absent. Flags are
     * recorded in the order they appear, although ordering has no semantic effect.
     * <p>
     * If a flag appears multiple times, the most recent occurrence overwrites any
     * previous one.
    */
    private final List<String> flags = new ArrayList<>();

    /**
     * Stores the collection of options supplied to the application.
     * <p>
     * Each option is treated as a key-value pair. Options are
     * recorded in the order they appear, although ordering has no semantic effect.
     * <p>
     * If an option appears multiple times, the most recent occurrence overwrites any
     * previous one.
    */
    private final Map<String, String> options = new HashMap();
    
    /**
     * TODO: REWRITE TO ALLOW long options and key value pair options
     * Parses the supplied command-line arguments and constructs an {@code Cliar}
     * instance containing the recognized flags.
     * <p>
     * Cliar supports both UNIX-style short options and GNU-style long options.
     * Short options use the {@code -x} syntax and may be grouped together
     * (e.g., {@code -xyz}), with each character representing a separate
     * alphabetic boolean flag. All short-option flags are normalized to
     * lower-case.
     * <p>
     * Long options use the {@code --key} or {@code --key=value} syntax.
     * If a long option is provided without a value, it is treated as a boolean
     * setting with an implicit value of {@code true}. Long-option keys are
     * normalized to lower-case. Any long option containing an {@code =} is
     * interpreted as a key–value pair.
     * <p>
     * If a flag or option appears multiple times, the most recent occurrence
     * overwrites any previous one.
     * <p>
     * Each supplied flag is treated as a boolean setting with an implicit value of
     * {@code true}.
     *
     * @param args the command-line arguments; each element must be one of the following forms:
     *              {@code -x}, {@code -xyz}, {@code --key}, or {@code --key=value}
     * @return an {@code Cliar} instance containing the parsed flags and options
     * @throws IllegalArgumentException if {@code args} is {@code null} or empty
    */
    public static Cliar from(String[] args) throws IllegalArgumentException {
        if ((null == args) || (0 == args.length)) {
            throw new IllegalArgumentException("String[] args is null.");
        }
        
        Cliar arguments = new Cliar();
        
        for (String arg : args) {
            if (arg.startsWith("--") && (2 < arg.length())) {
                arg = arg.substring(2);
                
                int index = arg.indexOf("=");
                if (-1 != index) {
                    String key = arg.substring(0, index);
                    String val = arg.substring(index + 1);
                    
                    arguments.options.put(key.toLowerCase(), val);
                } else {
                    arguments.options.put(arg, "true");
                }
            } else if (arg.startsWith("-") && !arg.startsWith("--")) {
                for (char chr : arg.substring(1).toCharArray()) {
                    if (Character.isLetter(chr)) {
                        arguments.flags.add(String.valueOf(Character.toLowerCase(chr)));
                    } else {
                        throw new IllegalArgumentException(String.format("Invalid option '%c' in argument %s.", chr, arg));
                    }
                }
            } else {
                throw new IllegalArgumentException(String.format("Invalid option %s.", arg));
            }
        }
        
        return arguments;
    }
}