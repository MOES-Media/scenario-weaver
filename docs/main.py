import xml.etree.ElementTree as ET

def define_env(env):
    tree = ET.parse('../pom.xml')
    root = tree.getroot()
    ns = {'m': 'http://maven.apache.org/POM/4.0.0'}
    version = root.find('m:version', ns)
    if version is not None:
        env.variables['maven_version'] = version.text
    else:
        env.variables['maven_version'] = 'UNKNOWN'