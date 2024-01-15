package kr.co.kfs.assetedu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kfs.assetedu.model.Com02Code;
import kr.co.kfs.assetedu.model.QueryAttr;
import kr.co.kfs.assetedu.repository.Com02CodeRepository;

@Service
public class Com02CodeService {
	
	@Autowired
	private Com02CodeRepository com02CodeRepository;
	
	public Com02Code selectOne(Com02Code code) {
		return com02CodeRepository.selectOne(code);
	}
	
	public List<Com02Code> selectList(QueryAttr queryAttr){	
		return com02CodeRepository.selectList(queryAttr);
	}
	
	public int insert (Com02Code com02Code) {
		return com02CodeRepository.insert(com02Code);
	}
	
	public List<Com02Code> codeList(String comCd){
		QueryAttr queryAttr = new QueryAttr();
		queryAttr.put("com02ComCd", comCd);
		queryAttr.put("com02CodeType", "D");
		queryAttr.put("com02UserYn", "true");
		return com02CodeRepository.selectList(queryAttr);
	}
	
	@Transactional
	public int update(Com02Code com02Code) {
		return com02CodeRepository.update(com02Code);
	}
	
	public int delete(Com02Code com02Code) {
		return com02CodeRepository.delete(com02Code);
	}
	
	public List<Com02Code> trCodeList (String trType){
		return com02CodeRepository.selectTrList(trType);
	}
	
}
