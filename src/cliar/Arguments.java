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
import java.util.List;

/**
 * Represents a collection of boolean command-line flags for an application.
 * <p>
 * This class provides simple parsing and storage of UNIX-style short options
 * (e.g., {@code -x}). Only single-character flags are supported, and each flag
 * is treated as a boolean value without an associated argument.
 * <p>
 * The class is designed as a lightweight, drop-in utility rather than a
 * full-featured command-line parsing framework.
 * 
 * @author onebitwonder
 */
public class Arguments {
    
    /**
     * Private constructor to prevent direct instantiation. Instances are created
     * exclusively through the {@link #from(String[])} factory method.
    */
    private Arguments() {
        ;
    }
    
    /**
     * Stores the collection of command-line flags supplied to the application.
     * <p>
     * Each flag is treated as a boolean setting: a flag is considered {@code true}
     * when present on the command line and {@code false} when absent. Flags are
     * recorded in the order they appear, although ordering has no semantic effect.
     * <p>
     * If a flag appears multiple times, the most recent occurrence overwrites any
     * previous one.
    */
    // Future versions may support flags with associated values (e.g., -o=file.txt).
    private final List<String> flags = new ArrayList<>();

    /**
     * Parses the supplied command-line arguments and constructs an {@code Arguments}
     * instance containing the recognized flags.
     * <p>
     * Arguments must follow the case-insensitive UNIX short-option format
     * (e.g., {@code -x}). Only single-character options are supported; long options
     * and options with values are not recognized. All flags are normalized to
     * lower-case. If a flag appears multiple times, the most recent occurrence
     * overwrites any previous one.
     * <p>
     * Each supplied flag is treated as a boolean setting with an implicit value of
     * {@code true}.
     *
     * @param args the command-line arguments; each element must be of the form
     *             {@code "-x"}
     * @return an {@code Arguments} instance containing the parsed flags
     * @throws IllegalArgumentException if {@code args} is {@code null} or empty
    */
    public static Arguments from(String[] args) throws IllegalArgumentException {
        if ((null == args) || (0 == args.length)) {
            throw new IllegalArgumentException("String[] args is null.");
        }
        
        Arguments arguments = new Arguments();
        
        for (String arg : args) {
            if (arg.startsWith("-")) {
                arg = arg.substring(1);
                
                if (1 < arg.length()) {
                    throw new IllegalArgumentException(String.format("Invalid option %s.", arg));
                } else {
                    arguments.flags.add(arg);
                }
            }
        }
        
        return arguments;
    }
}