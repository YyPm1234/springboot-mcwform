package zone.mcw.mcwzone.springbootmcwform.service.impl;

import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import zone.mcw.mcwzone.springbootmcwform.entity.Ban;
import zone.mcw.mcwzone.springbootmcwform.redis.RedisFunction;
import zone.mcw.mcwzone.springbootmcwform.service.IAdminService;
import zone.mcw.mcwzone.springbootmcwform.service.IBanService;
import zone.mcw.mcwzone.springbootmcwform.utils.GetValue;
import zone.mcw.mcwzone.springbootmcwform.utils.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @author by W4i
 * @date 2020/10/1 14:39
 */
@Service
public class AdminServiceImpl implements IAdminService {
	private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

}
