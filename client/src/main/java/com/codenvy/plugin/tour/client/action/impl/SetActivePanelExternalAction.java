/*******************************************************************************
 * Copyright (c) 2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/

package com.codenvy.plugin.tour.client.action.impl;

import com.codenvy.ide.api.parts.PartPresenter;
import com.codenvy.ide.api.parts.PartStack;
import com.codenvy.ide.api.parts.PartStackType;
import com.codenvy.ide.api.parts.WorkspaceAgent;
import com.codenvy.plugin.tour.client.action.ExternalAction;
import com.codenvy.plugin.tour.client.log.Log;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

/**
 * External actions for part presenters
 * @author Florent Benoit
 */
public class SetActivePanelExternalAction implements ExternalAction {

    /**
     * Logger.
     */
    @Inject
    private Log log;

    /**
     * Workspace Agent used to get part stack and set active one
     */
    @Inject
    private WorkspaceAgent workspaceAgent;


    /**
     * Accepts only "opentab" category
     * @param category which should be "opentab" to be accepted
     * @return true if category is opentab
     */
    @Override
    public boolean accept(String category) {
        return "opentab".equals(category);
    }

    /**
     * Opens the given tab
     * @param tabIdentifier the identifier based on the following : PartStackType.TITLE {@link com.codenvy.ide.api.parts.PartStackType}
     */
    @Override
    public void execute(@Nonnull String tabIdentifier) {


        int firstDot = tabIdentifier.indexOf('.');
        if (firstDot < 0) {
            log.info("Invalid tab identifier {0}", tabIdentifier);
            return;
        }

        String partStackTypeString = tabIdentifier.substring(0, firstDot);
        String title = tabIdentifier.substring(firstDot + 1);


        // get the partstack
        PartStack partStack = workspaceAgent.getPartStack(PartStackType.valueOf(partStackTypeString));
        if (partStack == null) {
            log.info("Invalid tab identifier " + tabIdentifier);
            return;
        }

        List<PartPresenter> presenters = partStack.getPartPresenters();
        if (presenters != null) {
            for (PartPresenter partPresenter : presenters) {
                if (title.equals(partPresenter.getTitle())) {
                    // found matching tab
                    workspaceAgent.setActivePart(partPresenter);
                }
            }
        }
    }
}
