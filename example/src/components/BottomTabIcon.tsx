import { StyleSheet, View } from 'react-native';

type BottomTabIconName = 'home' | 'key' | 'document';

type BottomTabIconProps = {
  name: BottomTabIconName;
  color: string;
  focused: boolean;
};

const BottomTabIcon = ({ name, color, focused }: BottomTabIconProps) => {
  if (name === 'home') {
    return (
      <View style={styles.iconContainer}>
        <View
          style={[
            styles.homeRoof,
            {
              borderLeftColor: 'transparent',
              borderRightColor: 'transparent',
              borderBottomColor: color,
            },
          ]}
        />
        <View style={[styles.homeBody, { borderColor: color }]}>
          <View style={[styles.homeDoor, { backgroundColor: color }]} />
        </View>
      </View>
    );
  }

  if (name === 'key') {
    return (
      <View style={styles.iconContainer}>
        <View style={[styles.keyHead, { borderColor: color }]} />
        <View style={[styles.keyStem, { backgroundColor: color }]} />
        <View style={[styles.keyTooth, { backgroundColor: color }]} />
      </View>
    );
  }

  return (
    <View style={styles.iconContainer}>
      <View style={[styles.documentBody, { borderColor: color }]}>
        <View
          style={[
            styles.documentFold,
            { borderTopColor: color, borderLeftColor: color },
          ]}
        />
      </View>
      {focused ? (
        <View style={[styles.documentLine, { backgroundColor: color }]} />
      ) : null}
    </View>
  );
};

const styles = StyleSheet.create({
  iconContainer: {
    width: 24,
    height: 24,
    alignItems: 'center',
    justifyContent: 'center',
  },
  homeRoof: {
    width: 0,
    height: 0,
    borderLeftWidth: 8,
    borderRightWidth: 8,
    borderBottomWidth: 8,
    marginBottom: 1,
  },
  homeBody: {
    width: 14,
    height: 10,
    borderWidth: 2,
    borderRadius: 2,
    alignItems: 'center',
    justifyContent: 'flex-end',
    paddingBottom: 1,
  },
  homeDoor: {
    width: 4,
    height: 5,
    borderRadius: 1,
  },
  keyHead: {
    width: 9,
    height: 9,
    borderRadius: 6,
    borderWidth: 2,
    position: 'absolute',
    left: 2,
  },
  keyStem: {
    width: 11,
    height: 2,
    borderRadius: 2,
    position: 'absolute',
    right: 2,
  },
  keyTooth: {
    width: 3,
    height: 4,
    borderRadius: 1,
    position: 'absolute',
    right: 2,
    top: 13,
  },
  documentBody: {
    width: 15,
    height: 18,
    borderWidth: 2,
    borderRadius: 3,
    position: 'relative',
    overflow: 'hidden',
  },
  documentFold: {
    position: 'absolute',
    top: 0,
    right: 0,
    width: 6,
    height: 6,
    borderTopWidth: 2,
    borderLeftWidth: 2,
    borderTopLeftRadius: 2,
  },
  documentLine: {
    position: 'absolute',
    width: 7,
    height: 2,
    borderRadius: 2,
    bottom: 5,
  },
});

export default BottomTabIcon;
