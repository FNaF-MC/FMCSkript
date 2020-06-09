# FMCSkript plugin
**An addon for Skript, adding support for required features on the FNaF MC server**

**_Created by Pdani001_**

### Current Skript additions:
- Door block checker:
  - `%block% is doorblock`
  - `%block% is opendoor`
- Nametag modify (requires NickNamer plugin):
  - `nametag of %player%`
- Sneak state changer:
  - `sneak[ing] [state] of %player%`
- Entity disguise (requires LibsDisguises plugin):
  - `[new] disguise [with] type %-string% [with block [id] %-itemstack%] [with armor %-string%] [(and|with) data [id] %number%] [with [user[ ]]name %-string%] [(and|with) adult [state] %-boolean%] [(and|with) visible [state] %-boolean%]`
    - **Please, make sure to call this and store the returned value, before using the "disguise of %entity%" expression**
  - `disguise of %entity%`
    - **Please, make sure to call the "\[new\] disguse type (...)" expression, and provide the stored value, before calling this**
  - `self disguise [visible] of %entity%`
    - The reset function will always set this to false, and if the player doesn't have a disguise this will also always return false