package org.jahia.modules.verifyintegrity.services.checks;

import org.jahia.api.Constants;
import org.jahia.modules.verifyintegrity.api.ContentIntegrityCheck;
import org.jahia.modules.verifyintegrity.services.ContentIntegrityError;
import org.jahia.modules.verifyintegrity.services.impl.AbstractContentIntegrityCheck;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

@Component(service = ContentIntegrityCheck.class, immediate = true, property = {
        ContentIntegrityCheck.ExecutionCondition.APPLY_ON_WS + "=" + Constants.LIVE_WORKSPACE,
        ContentIntegrityCheck.ExecutionCondition.SKIP_ON_NT + "=" + Constants.JAHIAMIX_MARKED_FOR_DELETION_ROOT,
        ContentIntegrityCheck.ExecutionCondition.APPLY_ON_NT + "=" + Constants.JAHIAMIX_MARKED_FOR_DELETION
})
public class MarkForDeletionCheck extends AbstractContentIntegrityCheck {

    private static final Logger logger = LoggerFactory.getLogger(MarkForDeletionCheck.class);

    @Override
    public ContentIntegrityError checkIntegrityBeforeChildren(Node node) {
        boolean isConsistent = true;
        Node parent = node;
        try {
            while (true) {
                try {
                    parent = parent.getParent();
                } catch (ItemNotFoundException e) {
                    isConsistent = false;
                    break;
                }
                if (!parent.isNodeType("jmix:markedForDeletion")) {
                    isConsistent = false;
                    break;
                }
                if (parent.isNodeType("jmix:markedForDeletionRoot")) break;
            }
            if (!isConsistent) {
                return ContentIntegrityError.createError(node, null, "The node is flagged as deleted, but the root of the deletion can't be found", this);
            }
        } catch (RepositoryException e) {
            logger.error("", e);
        }
        return null;
    }

    @Override
    public ContentIntegrityError checkIntegrityAfterChildren(Node node) {
        return null;
    }
}
