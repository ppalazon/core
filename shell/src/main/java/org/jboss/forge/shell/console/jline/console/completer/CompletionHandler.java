/*
 * Copyright (c) 2002-2007, Marc Prud'hommeaux. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */

package org.jboss.forge.shell.console.jline.console.completer;

import java.io.IOException;
import java.util.List;

/**
 * Handler for dealing with candidates for tab-completion.
 * 
 * @author <a href="mailto:mwp1@cornell.edu">Marc Prud'hommeaux</a>
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 2.3
 */
public interface CompletionHandler
{
   boolean complete(org.jboss.forge.shell.console.jline.console.ConsoleReader reader, List<CharSequence> candidates,
            int position) throws IOException;
}
