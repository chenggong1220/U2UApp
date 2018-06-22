package com.tools;

import java.util.List;

import com.dto.SettingDto;

public class CheckList2 {

	public static boolean check(List<String> userlist, SettingDto dto) {
		boolean b = false;
		if ((dto.username.equals((userlist.get(0)).substring(1))))
			if (dto.nickName.equals(((String) userlist.get(1)).substring(1)))
				if (dto.email.equals(((String) userlist.get(2)).substring(1)))
					if ((((String) userlist.get(3)).contains(dto.userType)))
						if (dto.province.equals((String) userlist.get(6)
								.substring(1)))
							if (dto.city.equals((String) userlist.get(7)
									.substring(1)))
								b = true;
		return b;
	}
}