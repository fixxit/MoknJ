package nl.it.fixx.moknj.bal.module.asset;

import nl.it.fixx.moknj.bal.RepositoryBal;
import nl.it.fixx.moknj.bal.RepositoryContext;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.repository.AssetRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AssetBalAspect extends RepositoryBal<AssetRepository> {

    private static final Logger LOG = LoggerFactory.getLogger(AssetBalAspect.class);

    private final AssetLinkBal linkBal;

    @Autowired
    public AssetBalAspect(RepositoryContext context, AssetLinkBal linkBal) {
        super(context.getRepository(AssetRepository.class));
        this.linkBal = linkBal;
    }

    @After("execution(* nl.it.fixx.moknj.bal.module.asset.AssetBal.delete (nl.it.fixx.moknj.domain.modules.asset.Asset, String, boolean)) && args(record, menuId, token, cascade)")
    public void deleteAfter(JoinPoint joinPoint, Asset record, String menuId, String token, boolean cascade) throws Throwable {
        Asset result = repository.findOne(record.getId());
        if (result != null && cascade) {
            // delete links
            linkBal.getAllLinksByRecordId(result.getId(),
                    token).stream().forEach((link) -> {
                        linkBal.delete(link);
                    });
        }
        LOG.info("A request was issued for a sample name: " + record);
    }

}
