package com.alessiodp.oreannouncer.common.blocks.objects;

import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.checkerframework.checker.nullness.qual.Nullable;

@NoArgsConstructor
@Data
@Accessors(chain = true)
public class Alert {
	private String messageUser = "";
	private String messageAdmin = "";
	private String messageConsole = "";
	
	private BlockData data;
	@Nullable private String sound = ConfigMain.ALERTS_SOUND_DEFAULT;
	@Nullable private String serverId;
	
	public Alert(String messageUser, String messageAdmin, String messageConsole) {
		this.messageUser = messageUser;
		this.messageAdmin = messageAdmin;
		this.messageConsole = messageConsole;
	}
}
