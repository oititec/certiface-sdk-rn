import Ionicons from 'react-native-vector-icons/Ionicons';

export type BottomTabIconName = 'home' | 'key' | 'document';

type IoniconName = keyof typeof Ionicons.glyphMap;

type BottomTabIconProps = {
  name: BottomTabIconName;
  color: string;
  focused: boolean;
};

const ICON_NAMES: Record<
  BottomTabIconName,
  { focused: IoniconName; unfocused: IoniconName }
> = {
  home: { focused: 'home', unfocused: 'home-outline' },
  key: { focused: 'key', unfocused: 'key-outline' },
  document: { focused: 'clipboard', unfocused: 'clipboard-outline' },
};

const BottomTabIcon = ({ name, color, focused }: BottomTabIconProps) => {
  const iconName = focused
    ? ICON_NAMES[name].focused
    : ICON_NAMES[name].unfocused;

  return <Ionicons name={iconName} size={focused ? 26 : 24} color={color} />;
};

export default BottomTabIcon;
