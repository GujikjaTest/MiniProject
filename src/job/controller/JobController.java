package job.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import job.domain.JobDTO;
import job.model.JobDAO;
import job.model.JobDAO_imple;
import utils.AlignUtil;

public class JobController {
	
	// field
	JobDAO jdao = new JobDAO_imple();

	// method
	
	// *** 희망직종 목록을 보여주고 반복처리 후 값을 뱉는 메소드 *** //
	public int jobShowList(Scanner sc) {
		
		jobListArray(); // 희망직종을 표 3 * 7 형태로 정렬
		
		int fk_job_id;
		do {
			/////////////////////////////////////////////
			System.out.print("▷ 직종 번호 입력 : ");
			String put_fk_job_id = sc.nextLine();
			
			if(put_fk_job_id.isBlank()) {
				System.out.println(">> [경고] 직종 번호는 필수 입력사항입니다. <<\n");
			}

			else {
				try {
					fk_job_id = Integer.parseInt(put_fk_job_id);
					if(fk_job_id > 21 || fk_job_id < 0) {
						System.out.println(">> [경고] 직종 번호는 1부터 21까지 가능합니다. <<\n");
					}
					else {
						break;
					}
				} catch(NumberFormatException e) {
					System.out.println(">> [경고] 직종 번호는 숫자로만 입력해주세요 <<\n");
				} // end of try~catch()-----------------
			}
			/////////////////////////////////////////////
		} while(true);
		// do~while()-----------------------
		
		return fk_job_id;
		
	} // end of public void jobShowList()-----------
	


	// *** 희망직종 목록을 보여주고 값을 뱉는 메소드 *** //
	public int jobUpdateList(int oldJobId, Scanner sc) {
			
		jobListArray(); // 희망직종을 표 3 * 7 형태로 정렬
		
		int fk_job_id;
		do {
			/////////////////////////////////////////////
			System.out.print("▷ 직종 번호 입력 : ");
			String put_fk_job_id = sc.nextLine();
			
			if(put_fk_job_id.isBlank()) {
				fk_job_id = oldJobId;
				break;
			}

			else {
				try {
					fk_job_id = Integer.parseInt(put_fk_job_id);
					if(fk_job_id > 21 || fk_job_id < 0) {
						System.out.println(">> [경고] 직종 번호는 1부터 21까지 가능합니다. <<\n");
					}
					else {
						break;
					}
				} catch(NumberFormatException e) {
					System.out.println(">> [경고] 직종 번호는 숫자로만 입력해주세요 <<\n");
				} // end of try~catch()-----------------
			}
			/////////////////////////////////////////////
		} while(true);
		// do~while()-----------------------
		
		return fk_job_id;
		
	} // end of public void jobShowList()-----------
		
		
	
	// *** 직종 리스트 목록을 이쁘게 보여주기를 하는 메소드 *** //
	private void jobListArray() {
		
		StringBuilder sb = new StringBuilder();
		List<JobDTO> jobDTO = new ArrayList<>();
		
		jobDTO = jdao.jobList(); // 희망직종 목록을 보여주는 메소드
		
		sb.append("=".repeat(17)+"< 희망직종 목록 >"+"=".repeat(17));
		for(int i=0; i<21; i++) {
			if(i%3==0) {
				sb.append("\n");
			}
		sb.append(jobDTO.get(i).getJob_id()+" "+jobDTO.get(i).getName()+"\t\t"+" "); 
		}// end of for()-------------------
		sb.append("\n"+"=".repeat(47));
		
		System.out.printf(AlignUtil.tab(sb).toString()+"\n");
		
	} // end of private void jobListArray()---------------
	

}
