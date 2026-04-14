package com.bookai.service;

import com.bookai.dto.ContinueWritingDTO;
import com.bookai.vo.ContinueWritingResultVO;

public interface AiWritingService {

    /**
     * AI续写书籍章节
     * @param dto 续写请求参数
     * @return 续写结果
     */
    ContinueWritingResultVO continueWriting(ContinueWritingDTO dto);
}
